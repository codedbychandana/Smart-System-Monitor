How to run it:

`git clone https://github.com/codedbychandana/Smart-System-Monitor.git`

`docker-compose up --build`


Smart system monitoring system built with Java Spring boot and containerized using Docker. It tracks metrics of your system such as memory and cpu loads using the OSHI library. CPU overloads are predicted and can be reduced using ML anomaly detection (Isolation Forest). When anomalous processes are terminated, logs are stored in Redis. The entire metrics are scraped and visualized using Prometheus and Grafana.
