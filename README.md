How to run it:

`git clone https://github.com/codedbychandana/Smart-System-Monitor.git`

`docker-compose up --build`

NOTE: You need to create an **application-secret.properties** file in the SAME folder as **application.properties** and it should have:


> redis.password= <your-password>

> redis.url= <your-redis-db-url>

Smart system monitoring system built with **Java Spring boot** and containerized using **Docker**. It tracks metrics of your system such as memory and cpu loads using the **OSHI** library. CPU overloads are predicted and can be reduced using **ML anomaly detection** (Isolation Forest). When anomalous processes are terminated, logs are stored in **Redis**. All metrics are scraped and visualized using **Prometheus and Grafana**.
