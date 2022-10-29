FROM ubuntu

COPY ./Crowdfunding .

EXPOSE 9000

CMD ["/bin/bash", "./Crowdfunding/bin/Crowdfunding"]
