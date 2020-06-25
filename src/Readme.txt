1)Titulli i projektit:
Sistemi për menaxhimin e fluturimeve ne aeroportin e Prishtines
“Adem Jashari”.

Përshkrim i shkurtër i projektit:
Ky projekt menaxhon fluturimet e aeroplanave në një aeroport,
 ku në vete përmban serverin si vendi nga i cili aeroplanat(kompani aeroplanash) 
 marrin informatë se kur një aeroplan mund të aterojë në aeroport apo kur mund ta bëjë fluturimin e tyre. 

2)Krijimi i projektit:

Projekti është krijuar në gjuhën java.
Downloadimet:node.js.  

3)Ekzekutimi i projektit:

Projekti përmban gjithsej 5 klasë, të cilat janë:Serveri, Client, Client2, JavaSoundRecorder dhe PlayAudio.
Serveri:Mund të themi është klasa kryesore sepse klientat(aeroplanat) bëjnë komunikimin me serverin(kontrolluesin e trafikut të ajrit).
Client dhe Client2:Janë aeroplana të caktuar të cilët qdo kohë janë në komunikim me serverin.
JavaSoundRecorder:Eshte një klasë e cila e shfrytzon pathi-in e ëav file-it për të regjistruar zërin e pilotit të aeroplanit të caktuar,
 po ashtu edhe të serverit në kuadër të komunikimit me zë.
PlayAudio:Eshte një klasë që nga vet emri tregon që e shfaq zërin e regjistruar, 
 për tu dëgjuar nga ana e klientave po ashtu edhe nga ana e serverit.

Ekzekutimi bëhet në këtë mënyrë:
I ekzekuton klasën Server.java, klasët Client.java dhe Client2.java.
Pra pas ekzutimit të klientave, serveri merr informacion që klientat në fjalë(Boeing77 dhe FranceaAir312) 
 kanë startu lidhjen me server dhe janë në lidhje me serverin për komunikim me shkrim dhe me zë.
Komunikimi bëhet me butonat përkatës, p.sh me shkrim:vetëm shkruajmë në hapsirën para butonit send dhe dërgimin e shkrimit
 e bëjmë me atë buton.Pra kur serveri shkruanë, mesazhi dërgohet te të gjithë klientat të cilët janë të lidhur me serverin.
Ndërsa kur shkruanë njëri nga klientat, mesazhi i shkon vetëm serverit me inicialet e klientit për ta ditur se nga kush ka marrë mesazhin.
Po ashtu edhe komunikimi me zë bëhet në atë menyrë, 
 ku klikojmë në butonin Regjistrimi i Audios dhe në një kohë prej 3 sekondash bëhet regjistrimi i zërit.
Dhe pas regjistrimit të zërit aktivizohet butoni Dergo Audio, i cili nuk aktivizohet pa u përfundu regjistrimi i audios.
Dhe si përfundim secilin nga klientat mund të shkëputet nga lidhja me serverin duke shkruar në hapsirën "exit" para butoni Send.
Dhe si mesazh do t'i vie serverit, që klienti në fjalë është shkëputur nga linja.
Ndërsa sa i përket komunikimit me video, ky komunikim është realizuar me html5,css dhe node.js
Ekzekutimi i saj bëhet në këtë mënyrë, ku së pari në cmd caktojmë path-in se ku gjindet projekti i cili përmban:html5,css dhe node.js 
dhe pastaj shkruajmë "node server.js" dhe si mesazh ta kthen per me hap localhost në google me portin e caktuar, në rastin tonë
"localhost:3000".
Komunikimi bëhet në atë mënyrë që se pari klikojmë në butonin:"Fillo komunikimin me video",
 ai buton na lidh direkt ,me localhost në "google".Pastaj krijojmë një room duke klikuar ndonjë numër dhe pastaj hapet video.
Lejohet qasja e 2 personave me një dhomë të caktuar, jo më shumë.

4)Autorët e projektit:

Ata që kontribuan në këtë projekt janë:
@Alban Berisha,
@Qendrim Demiraj dhe 
@Arti Sadikaj.
