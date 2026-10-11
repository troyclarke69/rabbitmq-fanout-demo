markdown
# Spring Boot & RabbitMQ Fanout EDA Demo

This repository implements a high-throughput **Event-Driven Architecture (EDA)** using **Spring Boot 3.x** and **RabbitMQ** orchestrating messages through an asynchronous **Fanout Exchange**.


[ HTTP POST (JSON) ] ──> [ OrderController ]
│
▼
[ OrderPublisher ]
│
▼ (Publishes once)
( order.fanout.exchange )
/                       
▼ (Duplicates)            ▼ (Duplicates)
[ order.billing.queue ]   [ order.fraud.queue ]
│                         │
▼                         ▼
[ BillingConsumer ]        [ FraudConsumer ]

## 🏗️ Architectural Design Choices
*   **Temporal Decoupling:** Inbound API order requests accept a JSON payload, immediately return a `200 OK` or `202 Accepted` status to the client, and hand execution over to background consumers asynchronously via messaging.
*   **Open-Closed Principle:** New services can subscribe to order events simply by binding a new queue to the fanout exchange. No code inside the core Order Service needs to be modified or redeployed.

## 🚀 Quick Start (Docker Compose)

### Prerequisites
*   Docker & Docker Compose installed
*   Git Bash, PowerShell, or Terminal access

### Running the Infrastructure
1. Build and boot the unified multi-container system from your root directory:
   ```bash
   docker compose up --build
1. Access tools and management panels at:
	• Spring Boot REST Engine: http://localhost:8080
	• RabbitMQ Management Console: http://localhost:15672 (Credentials: guest / guest)

🧪 Verifying the Flow Natively

Fire a standalone JSON request to the order ingest endpoint using curl:
bash
curl -X POST "http://localhost:8080/api/orders" \
     -H "Content-Type: application/json" \
     -d '{"orderId":"ORD-9988"}'
Use code with caution.

Expected Docker Container Logs:

text
 [Order Service] Emitting event for order: ORD-9988
 [Billing Service] Consumed order event -> Processing credit card payment: {"orderId":"ORD-9988", "status":"CREATED"}
 [Fraud Service] Consumed order event -> Running risk heuristics algorithms: {"orderId":"ORD-9988", "status":"CREATED"}
Use code with caution.

📊 Stress & Load Testing

To test ingestion speeds and queue durability under concurrent conditions, use the packaged ApacheBench (ab) Docker configuration.

1. Create the Payload File

Create a post_data.txt file in your root folder containing the template JSON string:
bash
echo '{"orderId":"STRESS-TEST"}' > post_data.txt
Use code with caution.

2. Run the Benchmark (Windows / Git Bash)

Execute the load test targeting the native container host network:
bash
docker run --rm -v ".://apps" -w //apps httpd:alpine ab -n 1000 -c 10 -p post_data.txt -T "application/json" "http://host.docker.internal:8080/api/orders"
Use code with caution.

3. Expected Metrics Baseline

• Failed requests: 0
• Non-2xx responses: 0
• Throughput: Exceeds high-volume thresholds due to the asynchronous non-blocking memory architecture of the RabbitMQ channel buffers.
