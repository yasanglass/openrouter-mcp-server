# OpenRouter MCP

An MCP server for OpenRouter.

## Tools

- `user-chat-completion`

## Build

Run the command below to build the server:

```shell
./gradlw installDist
```

The executable files will be located in `build/install/openrouter-mcp/bin`.

## Usage

```json
{
  "mcpServers": {
    "openrouter": {
      "command": "<absolute path to openrouter-mcp executable>",
      "args": [],
      "env": {
        "OPENROUTER_API_KEY": "<openrouter api key>"
      }
    }
  }
}
```