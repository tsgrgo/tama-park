import { defineConfig } from 'vite';
import { VitePWA } from 'vite-plugin-pwa';

export default defineConfig({
	plugins: [
		VitePWA({
			registerType: 'autoUpdate',
			injectRegister: 'auto',
			manifest: false,
			workbox: {
				cleanupOutdatedCaches: true,
				globPatterns: [
					'**/*.{js,css,html,png,svg,woff2,bin,webmanifest}'
				],
				navigateFallback: '/index.html'
			}
		})
	],
	server: {
		headers: {
			'Cross-Origin-Opener-Policy': 'same-origin',
			'Cross-Origin-Embedder-Policy': 'require-corp'
		}
	},
	preview: {
		headers: {
			'Cross-Origin-Opener-Policy': 'same-origin',
			'Cross-Origin-Embedder-Policy': 'require-corp'
		}
	}
});
