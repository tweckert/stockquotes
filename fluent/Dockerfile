#
# docker build -t fluentd-elasticsearch:latest .
#
# https://docs.fluentd.org/container-deployment/docker-compose#step-1-create-fluentd-image-with-your-config-+-plugin
# https://stackoverflow.com/questions/71120621/efk-system-is-build-on-docker-but-fluentd-cant-start-up#71126320
#

FROM fluent/fluentd:v1.15.2-debian-1.0

USER root

RUN gem uninstall -I elasticsearch && gem install elasticsearch -v 7.17.0 \
    && gem install fluent-plugin-elasticsearch --no-document --version 5.0.3

# NB: Docker COPY does not update files when rebuilding a container
#RUN rm -rf /fluentd/etc/fluent.conf
#COPY fluent.conf /fluentd/etc/

USER fluent
