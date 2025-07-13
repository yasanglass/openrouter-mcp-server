# OpenRouter MCP Server

[![CI](https://github.com/yasanglass/openrouter-mcp-server/actions/workflows/ci.yml/badge.svg)](https://github.com/yasanglass/openrouter-mcp-server/actions/workflows/ci.yml)

An MCP server for OpenRouter.

## Tools

- `user-chat-completion`

## Build

Run the command below to build the server:

```shell
./gradlw installDist
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