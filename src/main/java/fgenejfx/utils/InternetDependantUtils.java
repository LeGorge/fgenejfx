package fgenejfx.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import fgenejfx.exceptions.NameGeneratorException;

public class InternetDependantUtils {

	private static String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if
			// Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static String[] getRandomNames(int amount) throws NameGeneratorException {
		String[] nomes = new String[amount];
		int index = 0, cont = 0;

		while (nomes[amount - 1] == null) {
			int rarity = new Random().nextInt(3) + 1;
			String url = "http://random-name-generator.info/?n=25&g=2&st=" + rarity;
//			String url = "http://random-name-generator.info/random/?n=25&g=2&st="+rarity;

			String result = executePost(url, "");
			String aa = result.substring(result.lastIndexOf("<ol class=\"nameList\">"),
					result.lastIndexOf("</ol>"));

			String[] av = aa.split("<li>");
			if ((av.length < 25) || (++cont == 25)) {
				throw new NameGeneratorException();
			}
			for (int i = 1; i < av.length - 1; i++) {
				String[] str = av[i].trim().split(" ");
				for (String s : str) {
					nomes[index++] = s.trim();
					if (index == amount) {
						return nomes;
					}
				}
			}

		}
		return nomes;
	}

//	public static String[] getRandomNamesByApi(int amount) throws Exception{
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("https://randomuser.me/api/")
//				.queryParam("gender", "male")
//				.queryParam("inc", "name")
//				.queryParam("nat", "us,dk,fr,gb")
//				.queryParam("results", 36);
//		String str = target.request(MediaType.APPLICATION_JSON)
//				.get(String.class);
//		
//		ObjectMapper mapper = new ObjectMapper();
////		MappingIterator<Object> node = mapper.readerFor(Name.class).readValues(str);
////		node.forEachRemaining(System.out::println);
//		Negocio n = mapper.readValue(str, Negocio.class);
//		n.getResults().forEach(r->System.out.println(r.getName().getFirst()));
////		JsonNode node = mapper.readTree(str);
//		
////		node.
//		
////		System.out.println(r.readEntity(String.class));
////		System.out.println(r.readEntity(PersonModel.class));
//		
////		return response.stream().filter(m->{
////				return StringUtils.isAsciiPrintable(m.getName());
////			}).limit(amount).toArray(String[]::new);
//		
//		return null;
//	}

}
