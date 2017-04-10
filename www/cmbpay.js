var cordova = require('cordova');

module.exports = {
    hello: function() {
        return 'Hello World!';
    },
    pay: function(params, success, error) {
        cordova.exec(success, error, "CMBPayPlugin", "pay", [params]);
    }
};
