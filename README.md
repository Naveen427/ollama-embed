# ollama-embed
Demo project helps to get answers from pdf or json file.
Demo project that uses embeddinggemma for embeddings and gemma3 for Chat


This project required ollama running in your local 
with embeddinggemma for embeddings - https://ollama.com/library/embeddinggemma
and gemma3 chat - https://ollama.com/library/gemma3

place your pdf or json in the /resources/files and replace the file names in IngestionService.java
- <your-pdf-file-name> : PDF file name
- <your-json-file-name> : json file name.

Update the DB details in compose.yml to run pgvector DB in the container.
Update the DB properties in the application.properties file.
This project uses liquibase and it'll create the required DB schema.

Invoke the `POST /load-pdf` or `POST /load-json` file to ingest the file contents 
- Wait for the ingestion to complete, a log will be printed once the ingestion is completed.
Once the file contents are ingested, the chat function can answer the question about the file.

Invoke `GET /questions?question=<your-question-about-the-pdf or json>`
See examples in the client.http file.
