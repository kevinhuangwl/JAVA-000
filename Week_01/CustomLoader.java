public class CustomLoader extends ClassLoader{
	private File clzFile;
	public CustomLoader(File file) {
		clzFile = file;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
			byte[] data;
			try {
				data = Files.toByteArray(clzFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			byte[] decriptedData = new byte[data.length];
			for(int i=0; i<data.length; i++) {
				decriptedData[i] = (byte) (0xFF - data[i]);
			}
			
			return defineClass(decriptedData, 0, data.length);
	}
	
	public static void main(String[] args) throws Exception {
		CustomLoader loader = new CustomLoader(new File("e:/Hello.xlass"));
		Class<?> clz = loader.loadClass("Hello");
		
		Method m = clz.getMethod("hello");
		m.invoke(clz.newInstance());
	}
}
