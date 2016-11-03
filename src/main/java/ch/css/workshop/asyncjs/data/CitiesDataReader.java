package ch.css.workshop.asyncjs.data;


import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.concurrent.Promise;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CitiesDataReader {

    private final String fileName;

    public CitiesDataReader(String fileName) {
        this.fileName = fileName;
    }

    public Future<List<CityData>> getCities() {
        try {
            final URL cities = getClass().getResource(fileName);
            final Path citiesPath = Paths.get(cities.toURI());

            final FileReader reader = new FileReader(AsynchronousFileChannel.open(citiesPath));
            return reader.readFile();

        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException(e);
        }
    }


    private class FileReader implements CompletionHandler<Integer, Void> {

        public static final int BUFFER_SIZE = 8192;

        final AsynchronousFileChannel channel;
        final byte[] bytesBuffer = new byte[BUFFER_SIZE];
        final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        final ByteBuffer readBuffer = buffer.asReadOnlyBuffer();
        final Promise<List<CityData>> resultPromise = Promise.make();
        private final CityParser parser = new CityParser();
        private ArrayList<CityData> cities = new ArrayList<>();
        private int offset = 0;

        private FileReader(AsynchronousFileChannel channel) {
            this.channel = channel;
        }


        private void readNext() {
            buffer.rewind();
            readBuffer.rewind();
            channel.read(buffer, offset, null, this);
        }

        private Future<List<CityData>> readFile() {
            readNext();
            return this.resultPromise.future();
        }

        @Override
        public void completed(Integer bytesRead, Void aVoid) {

            if (bytesRead > 0) {
                readBuffer.get(bytesBuffer, 0, bytesRead);

                try {
                    String newPart = new String(bytesBuffer, 0, bytesRead, "ISO-8859-1");

                    cities.addAll(parser.parseNext(newPart));
                    this.offset += bytesRead;
                    this.readNext();
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }

            } else {
                this.resultPromise.success(List.ofAll(cities));
            }

        }

        @Override
        public void failed(Throwable throwable, Void aVoid) {
            throwable.printStackTrace();
        }
    }

}
