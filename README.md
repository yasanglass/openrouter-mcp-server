# OpenRouter MCP Server

[![CI](https://github.com/yasanglass/openrouter-mcp-server/actions/workflows/ci.yml/badge.svg)](https://github.com/yasanglass/openrouter-mcp-server/actions/workflows/ci.yml)

An MCP server for [OpenRouter](https://openrouter.ai).

## Tools

- `user-chat-completion`: Returns chat completion responses of given models from OpenRouter for the user message

## Build

Run the command below to build the server:

```shell
./gradlew installDist
```

The executable files will be located in `build/install/openrouter-mcp-server/bin`.

## Usage

```json
{
  "mcpServers": {
    "openrouter": {
      "command": "<absolute path to the openrouter-mcp-server executable>",
      "args": [],
      "env": {
        "OPENROUTER_API_KEY": "<openrouter api key>"
      }
    }
  }
}
```