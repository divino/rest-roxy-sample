xquery version "1.0-ml";

module namespace yourNSAlias = "http://marklogic.com/rest-api/resource/sample-1";

declare namespace roxy = "http://marklogic.com/roxy";
declare namespace rapi = "http://marklogic.com/rest-api";

(:
 : TEST: curl --anyauth --user admin:admin -X GET "http://localhost:28040/v1/resources/sample-1
 :)
declare 
%roxy:params("")
function yourNSAlias:get(
  $context as map:map,
  $params  as map:map
) as document-node()*
{
  map:put($context, "output-types", "application/xml"),
  map:put($context, "output-status", (200, "OK")),
  (: this will be the response body :)
  document { <hello>Get Method XML World!</hello> }
};

(:
 : TEST: curl --anyauth --user admin:admin -X PUT http://localhost:28040/v1/resources/sample -d ""
 :)
declare 
%roxy:params("")
function yourNSAlias:put(
    $context as map:map,
    $params  as map:map,
    $input   as document-node()*
) as document-node()?
{
  map:put($context, "output-types", "application/xml"),
  map:put($context, "output-status", (201, "Created")),
  (: this will be the response body :)
  document { <hello>Put Method XML World!</hello> }
};

(:
 : TEST: curl --anyauth --user admin:admin -X POST http://localhost:28040/v1/resources/sample -d ""
 :)
declare 
%roxy:params("")
%rapi:transaction-mode("update")
function yourNSAlias:post(
    $context as map:map,
    $params  as map:map,
    $input   as document-node()*
) as document-node()*
{
  map:put($context, "output-types", "application/json"),
  map:put($context, "output-status", (201, "Created")),
  (: this will be the response body :)
  document {
    object-node {
      "hello": "Post Method JSON World!"
    }
  }
};

(:
 : TEST: curl --anyauth --user admin:admin -X DELETE http://localhost:28040/v1/resources/sample-1
 :)
declare 
%roxy:params("")
function yourNSAlias:delete(
    $context as map:map,
    $params  as map:map
) as document-node()?
{
  map:put($context, "output-types", "application/json"),
  map:put($context, "output-status", (200, "OK")),
  (: this will be the response body :)
  document {
    object-node {
      "hello": "Delete Method JSON World!"
    }
  }
};
