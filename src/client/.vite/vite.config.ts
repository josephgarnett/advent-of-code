import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
// @ts-ignore
import path from 'node:path';

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    outDir: path.resolve(__dirname, '..', '..', '..', 'docs')
  },
  plugins: [react()],
})
