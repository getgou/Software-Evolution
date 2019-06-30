'use strict';

customElements.define('compodoc-menu', class extends HTMLElement {
    constructor() {
        super();
        this.isNormalMode = this.getAttribute('mode') === 'normal';
    }

    connectedCallback() {
        this.render(this.isNormalMode);
    }

    render(isNormalMode) {
        let tp = lithtml.html(`<nav>
    <ul class="list">
        <li class="title">
            <a href="index.html" data-type="index-link">frontend documentation</a>
        </li>
        <li class="divider"></li>
        ${ isNormalMode ? `<div id="book-search-input" role="search">
    <input type="text" placeholder="Type to search">
</div>
` : '' }
        <li class="chapter">
            <a data-type="chapter-link" href="index.html"><span class="icon ion-ios-home"></span>Getting started</a>
            <ul class="links">
                    <li class="link">
                        <a href="overview.html" data-type="chapter-link">
                            <span class="icon ion-ios-keypad"></span>Overview
                        </a>
                    </li>
                    <li class="link">
                        <a href="index.html" data-type="chapter-link">
                            <span class="icon ion-ios-paper"></span>README
                        </a>
                    </li>
                    <li class="link">
                        <a href="dependencies.html"
                            data-type="chapter-link">
                            <span class="icon ion-ios-list"></span>Dependencies
                        </a>
                    </li>
            </ul>
        </li>
        <li class="chapter modules">
            <a data-type="chapter-link" href="modules.html">
                <div class="menu-toggler linked" data-toggle="collapse"
                    ${ isNormalMode ? 'data-target="#modules-links"' : 'data-target="#xs-modules-links"' }>
                    <span class="icon ion-ios-archive"></span>
                    <span class="link-name">Modules</span>
                    <span class="icon ion-ios-arrow-down"></span>
                </div>
            </a>
            <ul class="links collapse"
            ${ isNormalMode ? 'id="modules-links"' : 'id="xs-modules-links"' }>
                    <li class="link">
                        <a href="modules/AppModule.html" data-type="entity-link">AppModule</a>
                            <li class="chapter inner">
                                <div class="simple menu-toggler" data-toggle="collapse"
                                    ${ isNormalMode ? 'data-target="#components-links-module-AppModule-7885487388a28301d27fda898f8fe792"' : 'data-target="#xs-components-links-module-AppModule-7885487388a28301d27fda898f8fe792"' }>
                                    <span class="icon ion-md-cog"></span>
                                    <span>Components</span>
                                    <span class="icon ion-ios-arrow-down"></span>
                                </div>
                                <ul class="links collapse"
                                    ${ isNormalMode ? 'id="components-links-module-AppModule-7885487388a28301d27fda898f8fe792"' : 'id="xs-components-links-module-AppModule-7885487388a28301d27fda898f8fe792"' }>
                                        <li class="link">
                                            <a href="components/AddUserDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">AddUserDialogComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/AppComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">AppComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/BuildDetailsComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">BuildDetailsComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/BuildsComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">BuildsComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/DeleteDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">DeleteDialogComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/LoginComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">LoginComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/MainComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">MainComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/MarkDoneDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">MarkDoneDialogComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/NewBuildComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">NewBuildComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/NewMaterialComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">NewMaterialComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/QrDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">QrDialogComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/ToolbarComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">ToolbarComponent</a>
                                        </li>
                                        <li class="link">
                                            <a href="components/UserTableComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules">UserTableComponent</a>
                                        </li>
                                </ul>
                            </li>
                            <li class="chapter inner">
                                <div class="simple menu-toggler" data-toggle="collapse"
                                    ${ isNormalMode ? 'data-target="#injectables-links-module-AppModule-7885487388a28301d27fda898f8fe792"' : 'data-target="#xs-injectables-links-module-AppModule-7885487388a28301d27fda898f8fe792"' }>
                                    <span class="icon ion-md-arrow-round-down"></span>
                                    <span>Injectables</span>
                                    <span class="icon ion-ios-arrow-down"></span>
                                </div>
                                <ul class="links collapse"
                                    ${ isNormalMode ? 'id="injectables-links-module-AppModule-7885487388a28301d27fda898f8fe792"' : 'id="xs-injectables-links-module-AppModule-7885487388a28301d27fda898f8fe792"' }>
                                        <li class="link">
                                            <a href="injectables/AuthService.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules"}>AuthService</a>
                                        </li>
                                </ul>
                            </li>
                    </li>
            </ul>
        </li>
        <li class="chapter">
            <div class="simple menu-toggler" data-toggle="collapse"
            ${ isNormalMode ? 'data-target="#classes-links"' : 'data-target="#xs-classes-links"' }>
                <span class="icon ion-ios-paper"></span>
                <span>Classes</span>
                <span class="icon ion-ios-arrow-down"></span>
            </div>
            <ul class="links collapse"
            ${ isNormalMode ? 'id="classes-links"' : 'id="xs-classes-links"' }>
                    <li class="link">
                        <a href="classes/Account.html" data-type="entity-link">Account</a>
                    </li>
                    <li class="link">
                        <a href="classes/AppPage.html" data-type="entity-link">AppPage</a>
                    </li>
                    <li class="link">
                        <a href="classes/Base.html" data-type="entity-link">Base</a>
                    </li>
                    <li class="link">
                        <a href="classes/Build.html" data-type="entity-link">Build</a>
                    </li>
                    <li class="link">
                        <a href="classes/Build-1.html" data-type="entity-link">Build</a>
                    </li>
                    <li class="link">
                        <a href="classes/BuildEntry.html" data-type="entity-link">BuildEntry</a>
                    </li>
                    <li class="link">
                        <a href="classes/Credentials.html" data-type="entity-link">Credentials</a>
                    </li>
                    <li class="link">
                        <a href="classes/Files.html" data-type="entity-link">Files</a>
                    </li>
                    <li class="link">
                        <a href="classes/Magic.html" data-type="entity-link">Magic</a>
                    </li>
                    <li class="link">
                        <a href="classes/Part.html" data-type="entity-link">Part</a>
                    </li>
                    <li class="link">
                        <a href="classes/PrintingData.html" data-type="entity-link">PrintingData</a>
                    </li>
                    <li class="link">
                        <a href="classes/PrintingData-1.html" data-type="entity-link">PrintingData</a>
                    </li>
                    <li class="link">
                        <a href="classes/S3Constants.html" data-type="entity-link">S3Constants</a>
                    </li>
                    <li class="link">
                        <a href="classes/User.html" data-type="entity-link">User</a>
                    </li>
                    <li class="link">
                        <a href="classes/UserListEntry.html" data-type="entity-link">UserListEntry</a>
                    </li>
                    <li class="link">
                        <a href="classes/Utils.html" data-type="entity-link">Utils</a>
                    </li>
            </ul>
        </li>
                <li class="chapter">
                    <div class="simple menu-toggler" data-toggle="collapse"
                        ${ isNormalMode ? 'data-target="#injectables-links"' : 'data-target="#xs-injectables-links"' }>
                        <span class="icon ion-md-arrow-round-down"></span>
                        <span>Injectables</span>
                        <span class="icon ion-ios-arrow-down"></span>
                    </div>
                    <ul class="links collapse"
                    ${ isNormalMode ? 'id="injectables-links"' : 'id="xs-injectables-links"' }>
                            <li class="link">
                                <a href="injectables/AuthService.html" data-type="entity-link">AuthService</a>
                            </li>
                            <li class="link">
                                <a href="injectables/BuildService.html" data-type="entity-link">BuildService</a>
                            </li>
                            <li class="link">
                                <a href="injectables/FileService.html" data-type="entity-link">FileService</a>
                            </li>
                            <li class="link">
                                <a href="injectables/PrintingDataService.html" data-type="entity-link">PrintingDataService</a>
                            </li>
                            <li class="link">
                                <a href="injectables/UserService.html" data-type="entity-link">UserService</a>
                            </li>
                    </ul>
                </li>
        <li class="chapter">
            <div class="simple menu-toggler" data-toggle="collapse"
            ${ isNormalMode ? 'data-target="#interceptors-links"' : 'data-target="#xs-interceptors-links"' }>
                <span class="icon ion-ios-swap"></span>
                <span>Interceptors</span>
                <span class="icon ion-ios-arrow-down"></span>
            </div>
            <ul class="links collapse"
            ${ isNormalMode ? 'id="interceptors-links"' : 'id="xs-interceptors-links"' }>
                    <li class="link">
                        <a href="interceptors/TokenInterceptor.html" data-type="entity-link">TokenInterceptor</a>
                    </li>
            </ul>
        </li>
        <li class="chapter">
            <div class="simple menu-toggler" data-toggle="collapse"
                 ${ isNormalMode ? 'data-target="#guards-links"' : 'data-target="#xs-guards-links"' }>
            <span class="icon ion-ios-lock"></span>
            <span>Guards</span>
            <span class="icon ion-ios-arrow-down"></span>
            </div>
            <ul class="links collapse"
                ${ isNormalMode ? 'id="guards-links"' : 'id="xs-guards-links"' }>
                <li class="link">
                    <a href="guards/AuthGuard.html" data-type="entity-link">AuthGuard</a>
                </li>
            </ul>
            </li>
        <li class="chapter">
            <div class="simple menu-toggler" data-toggle="collapse"
            ${ isNormalMode ? 'data-target="#miscellaneous-links"' : 'data-target="#xs-miscellaneous-links"' }>
                <span class="icon ion-ios-cube"></span>
                <span>Miscellaneous</span>
                <span class="icon ion-ios-arrow-down"></span>
            </div>
            <ul class="links collapse"
            ${ isNormalMode ? 'id="miscellaneous-links"' : 'id="xs-miscellaneous-links"' }>
                    <li class="link">
                      <a href="miscellaneous/variables.html" data-type="entity-link">Variables</a>
                    </li>
            </ul>
        </li>
        <li class="chapter">
            <a data-type="chapter-link" href="coverage.html"><span class="icon ion-ios-stats"></span>Documentation coverage</a>
        </li>
        <li class="divider"></li>
        <li class="copyright">
                Documentation generated using <a href="https://compodoc.app/" target="_blank">
                            <img data-src="images/compodoc-vectorise.svg" class="img-responsive" data-type="compodoc-logo">
                </a>
        </li>
    </ul>
</nav>`);
        this.innerHTML = tp.strings;
    }
});
