# Consolidator
This project handles all tagging and tag handling of different containers.

---
### Actions

* Get all tags available
```
curl -XGET {host}:{port}/Consolidator/resources/tags
```
* Add tag
```
curl -XPUT {host}:{port}/Consolidator/resources/tags/{tagName}
```
* Remove tag
```
curl -XDELETE {host}:{port}/Consolidator/resources/tags/{tagName}
```
* Add container
```
curl -XPOST {host}:{port}/Consolidator/resources/tags/{tagName}/containers/{containerName}
```
* Remove container
```
curl -XDELETE {host}:{port}/Consolidator/resources/tags/{tagName}/containers/{containerName}
```
