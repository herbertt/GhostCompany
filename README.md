# GhostCompany

Aplicativo mobile desenvolvido no III hackfest contra corrupção com objetivo de facilitar a identificação de empresas fantasmas com licitações na base do governo por meio da colaboração popular.


## Regras para contribuir

- Gerenciar atividades de desenvolvimento e teste através do PivotalTracker (https://www.pivotaltracker.com/n/projects/2056985);
- Integrar PivotalTracker e GitHub a fim de ser possível identificar os commits associados à uma tarefa;
- Padrão para mensagem de commit: <b>[#ID] message</b> e <b>[completed #ID] message</b>, sendo <b>ID</b> o identificador da tarefa no PivotalTracker;
- Tarefa de funcionalidade = User story do tipo feature do PivotalTracker;
- Tarefa de teste = User story do tipo chore do PivotalTracker;
- Padrão para título de tarefa de teste no PivotalTracker: <b>Test #ID_FEATURE</b>;
- Caso as tarefas de funcionalidade e teste sejam realizadas pela mesma pessoa, pode ser definida uma user story única no PivotalTracker para ambas;
- Commits que não estiverem relacionados à uma funcionalidade específica não devem ter ID;
- Um commit pode estar relacionado à mais de uma tarefa, o que significa que sua mensagem pode conter mais de um ID. Formato: <b>[#ID_1 #ID_2]</b>, sendo <b>ID_1</b> o identificador da tarefa 1 e <b>ID_2</b> o identificador da tarefa 2.
- Para importação do projeto no Android Studio, seguir o seguinte [Tutorial de Importação](https://github.com/herbertt/GhostCompany/blob/master/Docs/ImportProject.docx).

Em caso de dúvidas, entrar em contato com <b>Herbertt Diniz</b> (https://github.com/herbertt).



## BUILD SETUP

Gradle (API Android):

//Adicionar compiles

compile 'com.android.support:appcompat-v7:23.4.0'
compile 'com.google.android.gms:play-services:8.4.0'
compile 'com.google.code.gson:gson:2.3'
compile 'com.squareup.okhttp:okhttp:2.5.0'
compile 'com.googlecode.json-simple:json-simple:1.1.1'
compile 'com.google.maps.android:android-maps-utils:0.4'
compile 'com.android.support:support-v4:23.0.1'
compile 'com.android.support:design:23.0.1'
compile 'com.android.support:cardview-v7:23.0.1'
compile 'com.android.support:recyclerview-v7:23.0.1'
compile 'com.squareup.picasso:picasso:2.3.2'
compile 'com.nineoldandroids:library:2.4.0'
compile 'com.daimajia.slider:library:1.1.5@aar
