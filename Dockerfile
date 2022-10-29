FROM ubuntu

COPY ./Crowdfunding /usr/src/

WORKDIR /usr/src/Crowdfunding

EXPOSE 9000

CMD ["/bin/bash", "./bin/Crowdfunding"]
