# Answer File - Semester 2
# Description of each Implementation
Briefly describe your implementation of the different methods. What was your idea and how did you execute it? If there were any problems and/or failed implementations please add a description.

## Task 1 - mst

idè og implementasjon: Implementere prims algoritme som går ut på starte på en tilfeldig node og jobbe meg ufra den med å velge edgen med minst vekt. Lagre en oversikt over alle besøkte noder. Om edge kobler til en node som allerede er besøkt, vet vi at en annen edge med mindre vekt allerede er i bruk og vi kan derfor forkaste edgen. Fortsette til alle mulige edgene er undersøkt. 

problemer : Når jeg fant ut at jeg kunne gi priorityQeuen grafen g som comparator slik at toSearch gir ut edgen med lavest vekt falt resten av implementeringen rett på plass. Det var å finne frem metoder i en kodebase en ikke har skrevet selv som tok mest tid i starten. 

## Task 2 - lca
idè og implementasjon: Formålet er å finne den nærmeste(nedeste) felles ansestor for to nodes (u og v). Så tanken her var å finne deres path til root og se om/på hvilken node de veiene overlapper. Starter derfor med bredde først søk fra root og videre mapper alle kids som key og parent som value i et map. For å ikke mappe feil (ansestor til kid, holder jeg oversikten over allerede besøkte noder, så mapper kun "nedover") 

Etter å gått ha gjennom hele grafen har jeg no et hashmap som mapper hver node til dens nærmeste ansestor. Denne mappen bruker jeg til å lage et hashSet som inneholder alle nodene i pathen fra u til root. Så starter tar jeg den andre noden (v) og sjekker om den er i pathen, hvis ikke "klatrer" videre til v sin ansestor og sjekker om den er i grafen. Repeat til ansestoren er i pathen. Returner første ansestoren funnet. Siden jeg startet fra bunnen vil den første ansestore som er i pathen til u, være den laveste ansestoren til begge nodene. 

problem: Når jeg først kom fram til at jeg må mappe hver kid til dens nærmeste ansestor og ikke ansestor til den kid ble det plutselig veldig opplagt at en kan starte nedenifra og bruke mappet til å gå helt opp fra den ene noden. Og se om den andre nodens path overlapper med pathen som en lagret. 

## Task 3 - addRedundant
idè og implementasjon: Må finne edge som gir best mulig utfall ved verst mulig strøm brud. Siden grafen vi jobber med et et tre vil værste bruddet oppstå om en av edgene mellom rooten og dens nabooer bli brudt. Tanker her var om jeg fant de to største sub trærne i grafen og koblet de to sammen til å lage et størst mulig sykel vil dette være den mest optimale koblingen. Startet med å lage et hashmap, hvor hver key et roten til et sub tre og verdien er antall noder i det sub treet. Bruker dette til å finne de to største sub trærne. For å finne hvor jeg skal koble disse to sammen, må jeg lage størst mulig sykel og dette kan jeg få med å gå lenght ned i treet og koble sammen de to "leafs" som er "lengst unna" roten til subtreet.

problem: Tok en stund å skjønne hvilken løsning som blir raskest å implementere utfra oppgaven. Siden oppgave eksempelet viser en edge mellom to ansestors og ikke leafs. Implementerte oppgavene først rekursivt da jeg syns det falt mer naturlig og måtte jobbe en del får å bytte logikken over til en while løkke istedenfor. 

# Runtime Analysis
For each method of the different strategies give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented any helper methods you must add these as well.**

    n : number of nodes in graph
    m : number of edges in graph

* ``mst(WeightedGraph<T, E> g)``: O(m log n) 
    * Se kommentarer i kode for detaljert kjøretidsanalyse.

    ---

    Litt generelt: 

    Siden dette er en simple graph med ingen loops og ingen to nodes kan være koblet sammen av to forskjellige edges vet vi at: n antall noder kan være koblet sammen til n-1 andre noder. Og siden hver edge har to forskjellige noder i hver ende blir maks antallet edges m = n(n-1)/2
	det følger at:

        O(m) <= O(n**2) 
        O(log m) <= O(log n**2) = O(2log n) = O(log n)
        O(log m) = O(log n) (konstant forskjell)

    ---

    La oss se nærmere på deler av metoden:
    
        for (Edge<V> edge :g.adjacentEdges(node) ) {  	// O(nlogn)
                toSearch.add(edge); 	}
    
- Denne for-løkken itererer gjennom alle kanter (edges) som er koblet til den første noden i grafen. En node i en graf med n noder kan maksimalt være koblet sammen med n-1 andre noder. Dermed itererer for-løkken O(n-1) = O(n) ganger.

- Å legge til en en edge i en priorityQueue med metoden add() har en kjøretid på O(loga) hvor a er størrelsen på køen. Størrelsen på køen vil starte på 0 og vil i værste fall vokse til n-1 i denne løkken.

-  Denne løkken gjentas derfor O(n-1) ganger og hver add() operasjon har kjøretid på O(log (n-1)), noe som gir oss en total kjøretid på O(n log n)

    - Hele for løkken har en kjøretid på O(n log n)

    ---


        while (!toSearch.isEmpty()) { // O(m)			

- While løkken itereres 2m ganger, ettersom hver edge blir lagt til to ganger. Totalt : O(2m) = O(m). 

        Edge<V> minEdge = toSearch.remove();   // O(mlogm)		

 - Metoden remove() fjerner den minste edgen (den med lavest vekt) fra toSearch (en priorityQueue). Metoden remove() tar O(log a) hvor a er størrelse til køen. Totalt vil denne kjøre 2m ganger (ettersom hver edge kan legges til to ganger, en gang for hver node) og hvor remove har kjøretiden log(2m). Totalt O(mlogm)
	
        node = confirmedNodes.contains(minEdge.a) ? minEdge.b : minEdge.a;  // O(m)

- Sjekker om en node allerede er besøkt tar O(1) kjøretid, og gjøres 2m ganger. Totalt O(m)

        if (confirmedNodes.add(node)) { // O(m)		    	

- Siden vi holder styr på alle noder vi allerede har besøkt, vil betingelsen kun bli oppfylt n ganger. En gang for alle nodene i grafen, men den vil fremdeles sjekkes 2m times en gang for hver iterasjon av while løkken : Totalt O(m)

        for (Edge<V> edge : g.adjacentEdges(node)) { // O(m)	

- For løkken inni if-blokken gjøres en gang for hver node, og går gjennom alle kantene fra "sin side". Årsaken for at forløkken gjentas 2m times, er fordi hver edge vil bli iterert over to ganger, en gang fra en side og en gang fra noden på "andre siden" av kanten. ( edge (a,b) og (b,a) er samme edge ) : Totalt O(m)

        toSearch.add(edge); // O(mlogm)		 				
        }

- Igjen legge vi til en edge i toSeach, men i denne omgangen kan størrelsen på priority qeue være større. Dette har likevel ingenting å si, siden O(log2m) = O(logm)

- Total kjøretid for forløkken er derfor O(m) * O(logm) : Totalt O(m log m)

        confirmedVerticies.add(minEdge); //O(n)
        }		¨

- Resten av metoden er ganske rett frem. Vi legger til minste edgen n ganger (en gang for hver ny node): Totalt O(n)
	
    ---

    Kjøretids analyse for hele metoden:
    
    - Vi ser kun på største leddet. Det er enten for-løkken inne i while kodeblokken med kjøretiden O(m log m). Eller forløkken i starten av motoden med O(n log n). Vi fant ut i starten at O(log n) = O(log m). Kjøretiden avhenger altså av variabelen foran log og siden m <= n**2 følger at m > n.

    Totale kjøretiden er derfor: O(m log m)


---
---

* ``lca(Graph<T> g, T root, T u, T v)``: O(n)
    * Se kommentarer i kode for detaljert kjøretidsanalyse.

    Litt generelt: 

    Siden grafen er et tre vil antall edges være m = n-1. 
    
    La oss se nærmere på deler av metoden:

        HashMap<V,V> path = new HashMap<>(); // O(1)
		ArrayList<V> toSearch = new ArrayList<>(); // O(1)
		toSearch.add(root); // O(1)

- Alle operasjonene har konstant kjøretid O(1)

        while (i < toSearch.size()) { // O(n)
            V ansestor = toSearch.get(i++); // O(n)

 - While løkken gjentas for hvert element i toSearch ArrayListen, denne listen skal til slutt inneholde alle n nodene. Metoden get() har konstant kjøretid og kalles på for hver iterasjon av while løkken. Totalt : O(n)

            for (V child : g.neighbours(ansestor)) { // O(n)

- For løkken går gjennom alle naboene til en node. Siden en edge kobler sammen to noder i en graf, vil hver edge bli iterert 2 gangers. En gang fra ene side og en gang fra den andre siden. (Samme logikk som i analysen for mst). O(2m) = O(2(n-1)) = O(n)


            if (!path.containsKey(child)) {  //  O(n)
                path.put(child, ansestor); // O(n)
                toSearch.add(child); //  O(n)
            }

- Betingen sjekker om node er allerede mappet. Så betingelsen blir evaluert til true en gang for hver node i grafen. Alt inne i if kode blokken utføres derfor n ganger. Totalt : O(n)

- Total kjøretid for denne while løkken er O(n)

---

        HashSet<V> ansestorsU = new HashSet<>(); // O(1)
        while (!ansestorsU.contains(root)) { //  O(n)
            ansestorsU.add(u); // O(n)
            u = path.get(u); // O(n) 
        }

- Neste while løkke gjentas så mange ganger som det er noder mellom u og root. Worst case består grafen vår av bare en branch, hvor u er helt nederst. Dermed itererer while løkken totalt n ganger. Metodene add og get og contains har en konstant kjøretid. Totalt : O(n)

        while (!ansestorsU.contains(v)) { // O(n)
                v = path.get(v); // O(n)
            }

- For siste while løkken gjelder akuratt samme logikk, den skal gå gjennom stien fra v til root. Worst case ligger v helt nederst og  while løkken må iterere n ganger gjennom alle nodene for å finne root som lca til v og u før den bryter. Totalt : O(n)

    Vi ser på det største leddet i lca metoden og finner at O(n) er kjøretiden. 


---
---

    
* ``addRedundant(Graph<T> g, T root)``: O(m logm)
    * Se kommentarer i kode for detaljert kjøretidsanalyse.

    Hjelpe metoder 

    ---
    
     getSubtreeSizes()

    - Initialiseringen av objektene i starten har konstant kjøretid.O(1)

    - While løkken gjentas to ganger for hver node. Siden i første omgang blir hver node kun hentet ut med metoden peek() (denne har konstant kjøretide O(1)), men neste gang den samme noden blir hentet fra stacken blir den fjernet. Totalt O(n)

    - Betingelsen ekvivaleres til true kun n ganger, 1 gang for hver node. 

    - For løkken gjøres en gang for hver node og henter ut alle naboer. Likt begge oppgavene tidligere vet vi at, det som gjør at en node er nabo med en annen node er at de er koblet sammen med en edge. For løkken vil derfor gjentar 2m ganger. Etterfølgende betingelse sjekker også derfor 2m ganger, men evalueres kun til true n ganger, en gang for hver node. Siden push metoden har en konstant kjøretid blir kjøretiden her O(n) Totalt : O(n)

    - Akkurat samme kjøretids analyse gjelder for løkken i else kode blokken. Alle metode som getOrDefault(), put() og pop() har konstant kjøretid O(1). 

    getSubtreeSizes hjelpe funksjoner har en kjøretid på O(n)

    ---

    findLowestLeaf()

        int currentSize = subtreeSizes.get(node); // O(1)

            while (currentSize != 1) { // n times 
                V largestChild = null; // O(1)
                int largestChildSize = 0; // O(1)

    - Denne metoden skal finne den nederste noden (en leaf). Vi gjenstar dermed while løkken helt til størrelsen på subTreet er lik 1. Da vet vi at vi har funnet en leaf i grafen. Worst case må vi gjennom alle nodene for å finne denne leafen, while løkken itererer derfor n ganger. Dette skjer i tilfellet treet består av en lang branch, med en node per "nivå". 

            for (V child : g.neighbours(node)) {  //  O(n)
                int childSize = subtreeSizes.get(child); // O(n)
                

    - Igjen går vi gjennom alle naboene til noden. Helt likt som i de tre andre tilfellene itereres derfor for løken 2m ganger, hvor m er antall edges. Og get metoden har konstant kjøretid O(1). Totalt : O(m) = O(n)

            if (largestChildSize < childSize && childSize < currentSize) { // O(n)
                    largestChild = child; // O(n)
                    largestChildSize = childSize; // O(n)
                }
                }

    - Videre velger vi den noden som har ansestor til flest andre noder. Samtidig som vi ikke beveger oss opp igjen i grafen. Betingelsen som sjekker dette har konstant kjøretid, men Worst case evalueres dette til true kun n ganger. Altså i det tilfelle at grafen vår består bare av en lang branch, og vi velger kun vekk en node av gangen, slik at vi må gjennom alle nodene for å finne den nederste leafen. Totalt O(n)

    	    if (largestChild != null) { // n times O(1)
                    node = largestChild; // n times O(1)
                    currentSize = largestChildSize; // n times O(1)
                }

    - Så gitt at vi fant en desendant node som er "root" til den største mulige sub-treet endrer vi på noen variabler. Dette tar konstant tid, og som i forrige avsnitt kan i worst case gjentas n ganger.

    Største leddet for denne metoden er derfor O(n)

    --- 

    Klassen SubtreeSizeCmp<V> er veldig enkel og bruker Integer sin compare metode til å sammenligne hvilken av to gite noder er "root" til det største subtreet. Compare metoden sin kjøretid totalt O(1)

    ---- 

    addRedundant()

            HashMap<V, Integer> subtreeSizes = getSubtreeSizes(g, root); // O(n)


    - Kallet på hjelpemetoden fant vi ut hadde en kjøretid på O(n)

            if (g.degree(root) == 1) { // O(1)
                return new Edge<V>(root ,findLowestLeaf(g, root, subtreeSizes)); // O(n)
                }

    - I det tilfelle rooten kun har en nabo returneres en ny edge mellom rooten og noden som returneres av funksjonen findLowestLeaf. Hjelpemetoden har en kjøretid på O(n) og lage en ny edge har konstant kjøretid. Total O(n)

            V largest = null, secondLargest = null; 
		SubtreeSizeCmp<V> cmp = new SubtreeSizeCmp<>(subtreeSizes); // O(1)
		for (V node : g.neighbours(root)) { // O(n-1) = O(n)
			if (largest == null || cmp.compare(node, largest) == -1) { // O(1)
                secondLargest = largest; // O(1)
                largest = node;   // O(1)
                } else if (secondLargest == null || cmp.compare(node, secondLargest) == -1) { // O(1)
                        secondLargest = node;  // O(1)
                }
                        }
 - Finner de to nodene som er nabo med root. Worst case har root n-1 naboer. Dermed iterer for løkken n-1 ganger. Alle operasjonene i for løkken har konstant kjøretid. Totalt : O(n)

            V largestSubtreeLeaf = findLowestLeaf(g, largest, subtreeSizes); // O(n)
            V secondLargestSubtreeLeaf =  findLowestLeaf(g, secondLargest, subtreeSizes); // O(n)
    
    - Videre gjør vi to kall på findLowestLeaf metoden som har O(n) kjøretid. Vi gjør også kall på poll() metoden, denne har konstant kjøretid.

            return new Edge<V>(largestSubtreeLeaf,secondLargestSubtreeLeaf); // O(1)

    - Og til slutt returnere vi en ny edge, som har konstant O(1) kjøretid.

    - Totalt har metoden addRedundant O(n) kjøretid. 


