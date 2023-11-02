import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        try {
            // Чтение ссылки на фото из текстового файла
            BufferedReader br = new BufferedReader(new FileReader("links.txt"));
            String imageUrl = br.readLine();
            br.close();

            // Путь для сохранения фото
            String imageSavePath = "downloaded_image.jpg";

            // Создание пула потоков для параллельного скачивания
            ExecutorService executor = Executors.newFixedThreadPool(1);

            // Параллельное скачивание фото
            executor.execute(() -> {
                try {
                    downloadFile(imageUrl, imageSavePath);
                    System.out.println("Фото скачано");
                    openImage(imageSavePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Завершение работы пула потоков
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(String fileUrl, String savePath) throws IOException {
        URL url = new URL(fileUrl);
        try (InputStream in = url.openStream();
             FileOutputStream out = new FileOutputStream(savePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private static void openImage(String imagePath) {
        File imageFile = new File(imagePath);
        try {
            Desktop.getDesktop().open(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
