# POC - Store medias at Cloud Providers

## Descrição
Esse projeto tem como objetivo realizar uma prova de conceito(POC) para armazenar arquivos de mídia em provedores de nuvem da AWS (S3) e do GCP (GCS).

Estabelece uma conexão customizada com os buckets do S3 e GCS, permitindo que a API tenha algumas operações como:
- **Upload de imagens**: Funcionalidade para upload de imagens de diversos formatos para os buckets de armazenamento.
- **Busca de imagens**: Implementar filtros para busca de imagens por nome, data de upload, tamanho e outros critérios relevantes.
- **Exclusão de imagens**: Permitir a exclusão de imagens individuais ou em massa, de acordo com os requisitos do usuário.
- **Recuperação em Base64**: Funcionalidade para recuperar imagens no formato Base64, ideal para visualização direta em interfaces web ou mobile.
- **Download para diretório de assets**: Permitir o download de imagens selecionadas para um diretório de assets local, facilitando o acesso e utilização das imagens em diversas aplicações.

## Ferramentas necessárias
- Java 17+
- Spring Boot (para desenvolvimento da API REST)
- Conta na AWS e GCP (com permissões para manipulação de objetos no bucket)
- Alguma ferramenta para subir um Multipart Form Data (Postman, Insomnia, etc)

## Rodar aplicação
**OBS: Se estiver utilizando uma IDE tipo IntelliJ, pode configurar as variáveis de ambiente no Run Configuration.**

- Gerar as credenciais de acesso para AWS e GCP
    - **GCP**:
      - Crie ou acesse uma conta de serviço;
      - Dê permissões para manipular o bucket;
      - Gere uma chave e faça o download do arquivo JSON;
      - Converta o arquivo JSON para Base64;
        - `base64 -i <arquivo.json> -o <arquivo_base64.txt>`
      - Adicione como variável de ambiente:
        - `export GOOGLE_CLOUD_CREDENTIALS=<arquivo_base64.txt>`
      - Adicione o id do projeto GCP e o nome do bucket:
        - `export GOOGLE_CLOUD_PROJECT_ID=<id_projeto>`
        - `export GOOGLE_CLOUD_BUCKET=<nome_bucket>`
    - **AWS**:
      - Crie um chave de acesso e uma chave secreta com permissão para o S3;
      - Adicione como variável de ambiente:
        - `export AWS_ACCESS_KEY=<chave_acesso>`
        - `export AWS_SECRET_KEY=<chave_secreta>`
      - Adicione o nome do bucket:
        - `AWS_BUCKET=<nome_bucket>`
- Execute a aplicação para ele criar o banco de dados H2
- Pare a aplicação e execute o Flyway para criar as tabelas
  - `mvn flyway:migrate`
- Execute a aplicação novamente e está pronta para uso;

## Endpoints
- Salvar imagem:
  - POST - http://localhost:8080/images/upload?provider=gcp
    - `provider` (opcional)- Provedor de armazenamento (gcp ou aws)
    - `formData`:
      - `file` - Arquivo de imagem
- Listar imagens:
  - GET - http://localhost:8080/images
- Recuperar uma image em Base64:
  - GET - http://localhost:8080/images/base64/{id}
    - `id` - Id da imagem
- Download de imagem (ficará salva em uma paste assets na raíz do projeto):
  - GET - http://localhost:8080/images/download/{id}
    - `id` - Id da imagem
- Deletar imagem:
  - DELETE - http://localhost:8080/images/{id}
    - `id` - Id da imagem

## Algumas referências
https://aws.amazon.com/cli/

https://docs.aws.amazon.com/cli/latest/reference/configure/

https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-s3-objects.html#upload-object

https://cloud.google.com/storage/docs/downloading-objects-into-memory?hl=pt-BR#storage-download-object-java

https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/http-configuration-apache.html

https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html

https://cloud.google.com/storage/docs/samples/storage-upload-file?hl=en#storage_upload_file-java

https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/s3-checksums.html
