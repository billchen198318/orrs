// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  ssr: false, 
  devtools: { enabled: true },
  plugins: [
    '~/plugins/useBootstrap.client.ts'
  ],
  modules: [
    '@pinia/nuxt'
  ], /*
  routeRules: {
    '/dashboard'  : { ssr : false }
  }, */
  devServer: {
    port: 8077, // default: 3000
  }
})
