FROM node:24-alpine AS builder
WORKDIR /app
COPY ./package*.json .
RUN npm install
COPY . .
RUN npm run build



FROM nginx:1.31.4-alpine
RUN rm -f /etc/nginx/conf.d/default.conf
COPY --from=builder /app/build/ /var/www/html
RUN chown -R nginx:nginx /var/www/html && \
    chmod -R 555 /var/www/html
EXPOSE 80
