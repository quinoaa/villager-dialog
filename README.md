# Villager Dialog

Add quests to villagers

## Datapacks

### location

More dialogs can be added via datapacks. 

Dialogs are .json file under `dialogs` directory.

Ex: `data/datapack/dialogs/(id).json`

### format

A dialog follows a scheme: [Example](https://github.com/quinoaa/villager-dialog/tree/main/src/main/resources/data/villagerdialog/dialogs)

```json
{
  "first": "stepA",
  "dialogs": {
    "stepA": {
      "dialog": {
        "text": "Hello"
      },
      "choices": [
        {
          "dialog": {
            "text": "choice 1"
          },
          "goto": "choice1"
        },
        {
          "dialog": {
            "text": "choice 2"
          },
          "goto": "choice2"
        }
      ]
    },
    "choice1": {
      "dialog": {
        "text": "Choice 1"
      }
    },
    "choice2": {
      "dialog": {
        "text": "Choice 2"
      }
    }
  }
}
```

## Showcase

[![Showcase video](https://img.youtube.com/vi/hVk9v3b30iQ/0.jpg)](https://youtu.be/hVk9v3b30iQ)