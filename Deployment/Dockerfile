FROM node:latest

ARG MODE
ENV RUN_MODE="./${MODE}"
RUN echo "ENV MY MODE :: ${RUN_MODE}"

WORKDIR /usr/src/app
# Download the code from the internet shared location
#RUN wget -O ./assignment.tar.gz https://drive.google.com/file/d/16k1na8UA0THRBQbKSeo8t_spX1ehkXwx --no-check-certificate
#RUN tar -xzvf ./assignment.tar.gz

COPY package*.json /usr/src/app/
RUN npm install --only=production

# Bundle app source
COPY . .

CMD node app.js $RUN_MODE
