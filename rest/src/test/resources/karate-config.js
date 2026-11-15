function fn() {
  var port = karate.properties['karate.port'] || '8080';
  var baseUrl = 'http://localhost:' + port;
  var apiKey = karate.properties['karate.apiKey'] || 'ing-mortgage-local-key';
  var config = {
    baseUrl: baseUrl,
    apiUrl: baseUrl + '/api/v1',
    apiKey: apiKey
  };
  karate.configure('headers', { 'X-API-Key': apiKey });
  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);
  return config;
}

