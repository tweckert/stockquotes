# Getting Started

## Commands
* start Postgres container for development: `docker-compose -f docker-compose-postgres.yml up -d`
* build Docker image: `docker build -t stockquotes:latest .`
* build Docker image, start Postgres and app: `docker-compose up -d`

## How to run it
* Stockquotes app
  * [V1 chart for WKN 'A0RPWH': localhost:8080/chart/v1/A0RPWH](http://localhost:8080/chart/v1/A0RPWH)
  * [Prometheus actuator](http://localhost:8080/actuator/prometheus)
* [Portainer: localhost:9000](http://localhost:9000)
* [Prometheus: localhost:9090](http://localhost:9090)
  * [Prometheus stockquotes_app target](http://localhost:9090/targets?search=#pool-stockquotes_app)
* [Grafana: localhost:3000](http://localhost:3000)
  * [JVM Micrometer Dashboard](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/)

## Documentation
* [Google Charts Documentation](https://developers.google.com/chart/interactive/docs)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Prometheus Documentation](https://prometheus.io/docs/introduction/overview/)
* [Grafana Documentation](https://grafana.com/docs/)
* [Portainer Documentation](https://docs.portainer.io/)
