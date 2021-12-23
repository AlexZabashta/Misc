import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SerializeToString {

	static String serialize(Object object) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(data))) {
			oos.writeObject(object);
		} catch (IOException ignore) {
		}
		return Base64.getEncoder().encodeToString(data.toByteArray());
	}

	// WARNING! Never use this method if you received the string from an unknown source!
	static Object deserialize(String base64) throws ClassNotFoundException {
		byte[] data = Base64.getDecoder().decode(base64);
		try (ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(data)))) {
			return ois.readObject();
		} catch (IOException ignore) {
			return null;
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {

		List<String> list = new ArrayList<>();

		int n = 100;
		for (int i = 0; i < n; i++) {
			list.add(Integer.toString(i));
		}

		String string = serialize(list);

		System.out.println(string);

		@SuppressWarnings("unchecked")
		List<String> object = (List<String>) deserialize(string);
		System.out.println(list.equals(object));

	}
}
