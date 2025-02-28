const { defineConfig } = require('@vue/cli-service')
module.exports = ({
  configureWebpack: {
    resolve: {
      alias: {
        '@': require('path').resolve(__dirname, 'src')
      }
    }
  },
  devServer: {
    port: 3000
  }
})