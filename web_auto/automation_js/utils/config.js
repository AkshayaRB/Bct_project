/**
 * Configuration utility for environment-specific settings
 */
const config = {
  // Environment-based URL configuration
  urls: {
    base: process.env.BASE_URL || 'http://localhost:8001',
    login: process.env.LOGIN_URL || `${process.env.BASE_URL || 'http://localhost:8001'}/index.html`,
  },

  // Test data
  credentials: {
    valid: {
      username: process.env.VALID_USERNAME || 'testuser',
      password: process.env.VALID_PASSWORD || 'testpass',
    },
    invalid: {
      username: process.env.INVALID_USERNAME || 'invalid',
      password: process.env.INVALID_PASSWORD || 'invalid',
    },
  },

  // Timeouts (in milliseconds)
  timeouts: {
    navigation: 30000,
    element: 10000,
    action: 5000,
  },

  // Browser settings
  browser: {
    headless: process.env.HEADLESS === 'true' || process.env.HEADLESS === '1',
    slowMo: parseInt(process.env.SLOW_MO) || 0,
  },
};

module.exports = config;
