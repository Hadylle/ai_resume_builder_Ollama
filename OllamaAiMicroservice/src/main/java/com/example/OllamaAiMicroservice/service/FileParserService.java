package com.example.OllamaAiMicroservice.service;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileParserService {
    public String parseFile(MultipartFile file) throws IOException, TikaException, SAXException {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();

        try (InputStream stream = file.getInputStream()) {
            parser.parse(stream, handler, metadata);
            return handler.toString();
        }
    }
}