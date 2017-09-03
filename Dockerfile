FROM airhacks/payara-micro
MAINTAINER Royi Freifeld
ENV ARCHIVE_NAME Compass.war
COPY target/${ARCHIVE_NAME} ${INSTALL_DIR}