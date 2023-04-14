const express = require('express');
const prometheus = require('prom-client');

const app = express();

// Create a counter for the requests to the endpoint
const counter = new prometheus.Counter({
  name: 'target_1_requests_total',
  help: 'Number of requests to the target_1 endpoint'
});

// Define the endpoint to expose metrics
app.get('/metrics', (req, res) => {
  counter.inc(); // Increment the counter for each request
  res.send(prometheus.register.metrics());
});

// Start the server
app.listen(process.env.PORT, () => {
  console.log(`Metrics server listening on port ${process.env.PORT}`);
});
