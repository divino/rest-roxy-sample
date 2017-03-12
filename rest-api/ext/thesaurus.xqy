xquery version "1.0-ml";

module namespace yourNSAlias = "http://marklogic.com/rest-api/resource/thesaurus";

import module namespace thsr="http://marklogic.com/xdmp/thesaurus"
   at "/MarkLogic/thesaurus.xqy";
import module namespace json = "http://marklogic.com/xdmp/json"
   at "/MarkLogic/json/json.xqy";

declare namespace roxy = "http://marklogic.com/roxy";
declare namespace rapi = "http://marklogic.com/rest-api";

(:
 : Load thesaurus
 : Example: curl --anyauth --user admin:admin -d@"./data/thesaurus.xml" -H "Content-type: application/xml" -X POST "http://localhost:28040/v1/resources/thesaurus?rs:name=thesaurus.xml"
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
  let $name := map:get($params, "name")
  let $_ := map:put($context, "output-types", "application/json")
  return if ($name)
    then (
      thsr:insert($name, $input/element()),
      map:put($context, "output-status", (201, "Thesaurus successfully saved.")),
      document {
        object-node {
          "name": $name,
          "message": "Thesaurus loaded."
        }
      }
    )
    else (
      map:put($context, "output-status", (400, "Error loading thesaurus.")),
      document {
        object-node {
          "errorMessage": "Missing name."
        }
      }
    )
};

(: Retrieve synonyms.
 : Example: curl --anyauth --user admin:admin -X GET "http://localhost:28040/v1/resources/thesaurus?rs:word=bird&rs:thesaurus=thesaurus.xml
 :)
declare
%roxy:params("")
function yourNSAlias:get(
    $context as map:map,
    (: params would contain all request parametes with "rs:" prefix :)
    $params  as map:map
) as document-node()*
{
  let $word := map:get($params, "word")
  let $thesaurus := map:get($params, "thesaurus")
  let $_ := map:put($context, "output-types", "application/json")
  return if ($word and $thesaurus)
    then
      let $synonyms := thsr:lookup($thesaurus, $word)//thsr:synonym/thsr:term/fn:string()
      return document {
        object-node {
          "synonyms" : json:to-array($synonyms)
        }
      }
    else (
      map:put($context, "output-status", (400, "Error loading thesaurus.")),
      document {
        object-node {
          "errorMessage": "Missing 'word' or 'thesaurus' parameter."
        }
      }
    )
};
