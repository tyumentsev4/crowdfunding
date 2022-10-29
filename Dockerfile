FROM ubuntu

COPY ./Crowdfunding /usr/src/

WORKDIR /usr/src/Crowdfunding

RUN /bin/bash 'apt-get update && apt-get install -y jo openssl'

RUN /bin/bash -c 'jo -p salt=$(openssl rand -hex 128) > settings.json'

CMD ["/bin/bash", "bin/Crowdfunding"]
