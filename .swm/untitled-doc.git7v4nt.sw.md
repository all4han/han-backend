---
title: Untitled doc
---
<SwmSnippet path="/src/main/java/ex/han/backend/domain/controller/UserController.java" line="34">

---

&nbsp;

```java
    @PostMapping("/nickname-check")
    public ResponseEntity<? super UserNicknameDuplicateResponseDTO> userNicknameDuplicateCheck(@RequestBody @Valid UserNicknameDuplicateRequestDTO userNicknameDuplicateDTO){

        boolean rst = userService.userNicknameDuplicateCheck(userNicknameDuplicateDTO);

        return UserNicknameDuplicateResponseDTO.success(rst);

    }
```

---

</SwmSnippet>

<SwmMeta version="3.0.0" repo-id="Z2l0aHViJTNBJTNBaGFuLWJhY2tlbmQlM0ElM0FhbGw0aGFu" repo-name="han-backend"><sup>Powered by [Swimm](https://app.swimm.io/)</sup></SwmMeta>
