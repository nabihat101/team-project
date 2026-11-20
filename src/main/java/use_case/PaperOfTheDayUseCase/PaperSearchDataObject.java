package use_case.PaperOfTheDayUseCase;

import entities.ResearchPaper;

import java.util.List;

public class PaperSearchDataObject implements PaperSearchInterface {
    // fields: OkHttpClient client, String apiKey
    // (how many papers per interest + how many years back as constants)

    // constructor: make the client, load the API key (copy)

    // loadAPIKey(): read OPENALEX_API_KEY from api_key.properties (copy)

    // makeAPIRequest(url): send GET request, return the response body as a String (copy)

    @Override
    public List<ResearchPaper> searchRecent(List<String> interests) {


        // 1. make an empty list for results + a Set of ids already added (no duplicates)
        // 2. if interests is empty,  search for recent highly cited papers instead (3a)
        // 3. for each interest:
        //      - build the URL: search=<interest>, from_publication_date=<2 years ago>,
        //        sort=cited_by_count:desc, per_page=25, api_key
        //      - call makeAPIRequest (if IOException, throw RuntimeException,
        //        the interactor catches it and shows the "couldn't load" message)
        //      - turn the "results" array into ResearchPapers (parsePaper)
        //      - add each one whose id isn't in the Set yet
        // 4. return the list
        }

        // parsePaper(JSONObject paperJson): one OpenAlex result, ResearchPaper
        //   - id, title, publication_year
        //   - authors from "authorships" → raw_author_name   (copy loop)
        //   - url: best_oa_location → primary_location, doi (copy fallbacks)
        //   - abstract from rebuildAbstract(...)

        // rebuildAbstract(JSONObject invertedIndex): OpenAlex gives {word: [positions]}
        //   - if null → return ""
        //   - make a String array as big as the highest position + 1
        //   - for each word, put it at each of its positions
        //   - join with spaces and return
    }

}
