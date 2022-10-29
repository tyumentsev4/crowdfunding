FROM ubuntu

COPY ./Crowdfunding /usr/src/

WORKDIR /usr/src/Crowdfunding

RUN apt-get update && apt-get install -y jo openssl

RUN jo -p salt=$(openssl rand -hex 128) > settings.json

CMD ["./bin/Crowdfunding"]
