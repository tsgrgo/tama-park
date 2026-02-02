/**
 * This class provides a template for an application. Applications must be created by inheriting this class.
 * This class defines the normal application life cycle. It is also possible to obtain the download source URL
 * and ADF parameters from the JAM.
 */
export abstract class IApplication {
	private static currentApp?: IApplication;
	private static sourceUrl?: string;

	private args: string[] = [];

	protected constructor() {
		IApplication.currentApp = this;

		window.addEventListener('beforeunload', () => {
			this.terminate();
		});
	}

	public static getCurrentApp(): IApplication | null {
		if (!this.currentApp) {
			throw new Error('getCurrentApp(): Application has not yet been created');
		}
		return this.currentApp;
	}

	public abstract start(): void;
	public abstract resume(): void;

	protected getSourceURL(): string {
		return IApplication.sourceUrl || '';
	}

	public static setSourceURL(sourceUrl: string) {
		this.sourceUrl = sourceUrl;
	}

	protected getArgs(): string[] {
		return this.args.slice();
	}

	public launch(type: number, args: string[] | null): void {
		if (args) this.args = args.slice();
		console.log('IApplication.launch type=', type, 'args=', this.args);
	}

	public terminate(): void {
		console.log('terminated');
	}
}
