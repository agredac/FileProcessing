package tech.cagreda.parallel;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/*@Service
@EnableAsync
@RequiredArgsConstructor*/
public class FileProcessorService {

    /*private final MongoTemplate mongoTemplate;

    public void processFile(String inputFile, String outputFile) {

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String headerLine = reader.readLine();
            writer.write(headerLine + ",mongoField1,mongoField2\n");

            Stream<String> lines = reader.lines();

            // Process in parallel with batching
            List<CompletableFuture<String>> futures = new ArrayList<>();
            lines.forEach(line -> {
                CompletableFuture<String> future = processLineAsync(line);
                futures.add(future);
                if (futures.size() >= 1000) { // Write every 1000 records
                    writeBatch(writer, futures);
                    futures.clear();
                }
            });

            // Final batch write
            writeBatch(writer, futures);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public CompletableFuture<String> processLineAsync(String line) {
        String[] fields = line.split(",");
        String id = fields[0]; // Assuming the ID is in the first column

        // Fetch data from MongoDB based on ID
        Document result = mongoTemplate.getCollection("yourCollection").find(Filters.eq("id", id)).first();

        if (result != null) {
            return CompletableFuture.completedFuture(
                    line + "," + result.getString("mongoField1") + "," + result.getString("mongoField2"));
        } else {
            return CompletableFuture.completedFuture(line + ",,"); // If no MongoDB data found
        }
    }

    private void writeBatch(BufferedWriter writer, List<CompletableFuture<String>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures.forEach(future -> {
            try {
                writer.write(future.join() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }*/
}
