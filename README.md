# Getting Started

## Commands
* Development:
  * start Postgres container for development: `docker-compose -f docker-compose-postgres.yml up -d`
* Stockquotes service:
  * build Docker image: `docker build -t stockquotes:latest .`
* fluentd with Elasticsearch plugin:
  * build Docker image: `cd fluent && docker build -t fluentd-elasticsearch:latest .`
* Docker: 
  * Start all services: `docker-compose up -d`
  * Delete all containers: `docker rm -f $(docker ps -a -q)` 
  * Delete all volumes: `docker volume rm $(docker volume ls -q)`

## How to run it
* Stockquotes app
  * V1 chart for WKN 'A0RPWH': [http://localhost:8080/chart/v1/A0RPWH](http://localhost:8080/chart/v1/A0RPWH)
  * V1 chart for WKN 'A0YEDG' with range year-to-date and weekly frequency: [localhost:8080/chart/v1/A0YEDG?range=ytd&frequency=weekly](http://localhost:8080/chart/v1/A0YEDG?range=ytd&frequency=weekly)
  * Prometheus actuator: [http://localhost:8080/actuator/prometheus](localhost:8080/actuator/prometheus)
* Portainer: [localhost:9000](http://localhost:9000)
* Prometheus:[localhost:9090](http://localhost:9090)
  * Prometheus stockquotes_app target: [http://localhost:9090/targets?search=#pool-stockquotes_app](localhost:9090/targets?search=#pool-stockquotes_app)
* Grafana: [localhost:3000](http://localhost:3000)
  * [JVM Micrometer Dashboard](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/)
* Elasticsearch: [localhost:9200](http://localhost:9200)
  * List all indices: [localhost:9200/_cat/indices](http://localhost:9200/_cat/indices)
  * View all `logstash-*` indices: [localhost:9200/logstash-*?pretty](http://localhost:9200/logstash-*?pretty)
* Kibana: [localhost:5601](http://localhost:5601)
  * Create an index pattern `logstash-*`
* pgAdmin: [localhost:5050](http://localhost:5050) 
  
## Documentation
* [Google Charts](https://developers.google.com/chart/interactive/docs)
* [Apache Maven](https://maven.apache.org/guides/index.html)
* [Prometheus Timeseries](https://prometheus.io/docs/introduction/overview/)
* [Grafana Dashboards](https://grafana.com/docs/)
* [Portainer Container Management](https://docs.portainer.io/)
* [Micrometer monitoring](https://micrometer.io/)
* [fluentd logging](https://docs.fluentd.org/)
* [The Twelve-Factor App](https://12factor.net/)
* [Elasticsearch 7.x](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/elasticsearch-intro.html)
