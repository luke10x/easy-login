FROM node:18-bookworm

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .
#
## Define the default command to run your build
#CMD ["npm", "run", "build"]
