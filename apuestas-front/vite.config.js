// import { defineConfig } from 'vite'
// import react from '@vitejs/plugin-react'

// export default defineConfig({
//   plugins: [react()],
//   server: {
//     proxy: {
//       '/api/v1': {
//         target: 'http://localhost:8080',
//         changeOrigin: true,
//       },
//     },
//   },
// })


import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/v1': {
        target: 'http://app:8080',   // <- NO localhost, usa el nombre del servicio del backend
        // target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
