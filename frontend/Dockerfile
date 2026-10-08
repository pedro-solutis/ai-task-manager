# Build Stage
FROM node:20-alpine AS builder

WORKDIR /app

ARG VITE_BASE_URL
ENV VITE_BASE_URL=$VITE_BASE_URL

COPY package.json package-lock.json* ./

RUN npm install

COPY . .

RUN npm run build

# Web Server
FROM nginx:alpine

RUN rm /etc/nginx/conf.d/default.conf

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

