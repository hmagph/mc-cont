#  Copyright 2015 IBM
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.

FROM registry-ice.ng.bluemix.net/ibmliberty
#FROM ibmliberty
MAINTAINER Philippe Mulet "philippe_mulet@fr.ibm.com"

# Setup logging
RUN apt-get update &&\
    apt-get install -y ntp &&\
    apt-get install -y apt-transport-https

# prepare repository
RUN sudo -s &&\
    cd /etc/apt/trusted.gpg.d &&\
    wget https://logmet.opvis.bluemix.net:5443/apt/BM_OpVis_repo.gpg
RUN echo "deb https://logmet.opvis.bluemix.net:5443/apt stable main" > /etc/apt/sources.list.d/BM_opvis_repo.list
RUN apt-get update  &&\
    apt-get install -y apt-transport-https &&\
    apt-get install -y ntp &&\
    apt-get install -y mt-logstash-forwarder

COPY ibmliberty.conf /etc/mt-logstash-forwarder/conf.d/ibmliberty.conf
COPY mt-logstash-forwarder.conf /etc/supervisor/conf.d/mt-logstash-forwarder.conf

# create the server
#RUN bash -c "server create wordcounter"
# Install the application
ENV WEB_PORT 25565
EXPOSE  25565

# set this server as the default server for the liberty image
# ADD server.xml /opt/ibm/wlp/usr/servers/defaultServer/server.xml
ADD mcpatched.jar /opt/ibm/wlp/usr/servers/defaultServer/apps/mcpatched.jar
