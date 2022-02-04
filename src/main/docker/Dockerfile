FROM regnode.hzsh:5000/redhat-openjdk-18/openjdk18-openshift
MAINTAINER dhcc 

ENV LANG en_US.utf8

EXPOSE 7000

ENV TZ="Asia/Shanghai"
RUN date

COPY hzsh-authority-0.0.1.jar /usr/local/
ENTRYPOINT ["java" ,"-jar","/usr/local/hzsh-authority-0.0.1.jar"]