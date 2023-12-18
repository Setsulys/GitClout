const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  //transpileDependencies: true,
    devServer:{
        port:3080,
        publicPath: process.env.NODE_ENV === 'production' ? '/' : '/src/',
        proxy:{
            "/app*":{
                target:"http://localhost:8080",
                ws:true,
                changeOrigin:true,
//                pathRewrite: {
//                    '^/app': '/app'
//                  }
            }
        }
    }
})
