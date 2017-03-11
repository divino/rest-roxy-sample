xquery version "1.0-ml";

module namespace yourNSAlias = "http://marklogic.com/rest-api/resource/crud";

declare namespace roxy = "http://marklogic.com/roxy";
declare namespace rapi = "http://marklogic.com/rest-api";

(:
 : TEST: curl --anyauth --user <username>:<password> -X <http-method-upper-case> "http://<marklogic-hostname>:<application-rest-port>/v1/resources/<filename>?rs:<param-name>=<param-value>
 : NOTE: the rest parameters requires "rs:" prefix.
 :
 : All methods will return a json document which will return the content based on it's type.
 :)

(: Retrieve document content.
 : Example: curl --anyauth --user admin:admin -X GET "http://localhost:28040/v1/resources/crud?rs:uri=sample.json
 :)
declare 
%roxy:params("uri=xs:string")
function yourNSAlias:get(
  $context as map:map,
  (: params would contain all request parametes with "rs:" prefix :)
  $params  as map:map
) as document-node()*
{
  let $uri := map:get($params, "uri")
  let $doc := fn:doc($uri)
  (: check if uri parameter was passed and document uri exists :)
  return if ($uri and $doc)
    then (
      map:put($context, "output-types", xdmp:uri-content-type($uri)),
      map:put($context, "output-status", (200, "Success")),
      fn:doc($uri)
    )
    else (
      map:put($context, "output-types", "application/json"),
      map:put($context, "output-status", (404, "Document not found.")),
      document {
        object-node {
        "errorMessage": fn:concat("Document with uri'", $uri, "' not found.")
        }
      }
    )
};

(: Create or overwrite document.
 : EXAMPLE: curl --anyauth --user admin:admin -X POST "localhost:28040/v1/resources/crud?rs:uri=juan_de_la_cruz.json" --header 'content-type: application/json' -d '{ "name":"juan de la cruz", "address": "metro manila, philippines" }'
 :)
declare 
%roxy:params("")
(: %rapi:transaction-mode("update")
 : is required for POST method if the operation will change anything in the database.
 :)
%rapi:transaction-mode("update")
function yourNSAlias:post(
    $context as map:map,
    (: params would contain all request parametes with "rs:" prefix :)
    $params  as map:map,
    (: input is the body of the request :)
    $input   as document-node()*
) as document-node()*
{
  let $uri := map:get($params, "uri")
  return (
    map:put($context, "output-types", "application/json"),
    map:put($context, "output-status", (201, "Document saved")),
    xdmp:document-insert(
        $uri,
        $input,
        xdmp:default-permissions(),
        ("my-collections", "files")
    ),
    document {
      object-node {
        "uri": $uri,
        "content": xdmp:from-json($input)
      }
    }
  )
};

(: Delete document.
 : EXAMPLE: curl --anyauth --user admin:admin -X DELETE "localhost:28040/v1/resources/crud?rs:uri=juan_de_la_cruz.json"
 :)
declare 
%roxy:params("")
function yourNSAlias:delete(
    $context as map:map,
    (: params would contain all request parametes with "rs:" prefix :)
    $params  as map:map
) as document-node()?
{
  let $uri := map:get($params, "uri")
  let $doc := fn:doc($uri)
  let $_ := map:put($context, "output-types", "application/json")
  (: check if uri parameter was passed and document uri exists :)
  return if ($uri and $doc)
    then (
      xdmp:document-delete($uri),
      map:put($context, "output-status", (200, "Delete successful")),
      document {
        object-node {
          "message": fn:concat("Successfully deleted document with uri'", $uri, "'")
        }
      }
    )
    else (
      map:put($context, "output-status", (400, "Delete failed.")),
      document {
        object-node {
          "errorMessage": fn:concat("Document with uri'", $uri, "' not found.")
        }
      }
    )
};
