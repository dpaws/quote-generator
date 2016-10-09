FROM dpaws/microtrader-base
MAINTAINER Justin Menga <justin.menga@gmail.com>
LABEL application=quote-generator

# Copy application artefacts
ARG app_version
LABEL application.version=${app_version}
COPY target/quote-generator-${app_version}-fat.jar /app/app.jar