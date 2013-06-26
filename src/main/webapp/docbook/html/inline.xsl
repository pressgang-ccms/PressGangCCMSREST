<?xml version='1.0'?>
<!DOCTYPE xsl:stylesheet [
        <!-- ********************************************************************
             $Id: entities.ent 9286 2012-04-19 10:10:58Z bobstayton $
             ********************************************************************

             This file contains common entity declarations used for
             sorting (and other things) by various templates.

             This file is part of the XSL DocBook Stylesheet distribution.
             See ../README or http://docbook.sf.net/release/xsl/current/ for
             copyright and other information.

             ******************************************************************** -->

        <!-- ==================================================================== -->

        <!ENTITY lowercase "'Aa&#192;&#224;&#193;&#225;&#194;&#226;&#195;&#227;&#196;&#228;&#197;&#229;&#256;&#257;&#258;&#259;&#260;&#261;&#461;&#462;&#478;&#479;&#480;&#481;&#506;&#507;&#512;&#513;&#514;&#515;&#550;&#551;&#7680;&#7681;&#7834;&#7840;&#7841;&#7842;&#7843;&#7844;&#7845;&#7846;&#7847;&#7848;&#7849;&#7850;&#7851;&#7852;&#7853;&#7854;&#7855;&#7856;&#7857;&#7858;&#7859;&#7860;&#7861;&#7862;&#7863;Bb&#384;&#385;&#595;&#386;&#387;&#7682;&#7683;&#7684;&#7685;&#7686;&#7687;Cc&#199;&#231;&#262;&#263;&#264;&#265;&#266;&#267;&#268;&#269;&#391;&#392;&#597;&#7688;&#7689;Dd&#270;&#271;&#272;&#273;&#394;&#599;&#395;&#396;&#453;&#498;&#545;&#598;&#7690;&#7691;&#7692;&#7693;&#7694;&#7695;&#7696;&#7697;&#7698;&#7699;Ee&#200;&#232;&#201;&#233;&#202;&#234;&#203;&#235;&#274;&#275;&#276;&#277;&#278;&#279;&#280;&#281;&#282;&#283;&#516;&#517;&#518;&#519;&#552;&#553;&#7700;&#7701;&#7702;&#7703;&#7704;&#7705;&#7706;&#7707;&#7708;&#7709;&#7864;&#7865;&#7866;&#7867;&#7868;&#7869;&#7870;&#7871;&#7872;&#7873;&#7874;&#7875;&#7876;&#7877;&#7878;&#7879;Ff&#401;&#402;&#7710;&#7711;Gg&#284;&#285;&#286;&#287;&#288;&#289;&#290;&#291;&#403;&#608;&#484;&#485;&#486;&#487;&#500;&#501;&#7712;&#7713;Hh&#292;&#293;&#294;&#295;&#542;&#543;&#614;&#7714;&#7715;&#7716;&#7717;&#7718;&#7719;&#7720;&#7721;&#7722;&#7723;&#7830;Ii&#204;&#236;&#205;&#237;&#206;&#238;&#207;&#239;&#296;&#297;&#298;&#299;&#300;&#301;&#302;&#303;&#304;&#407;&#616;&#463;&#464;&#520;&#521;&#522;&#523;&#7724;&#7725;&#7726;&#7727;&#7880;&#7881;&#7882;&#7883;Jj&#308;&#309;&#496;&#669;Kk&#310;&#311;&#408;&#409;&#488;&#489;&#7728;&#7729;&#7730;&#7731;&#7732;&#7733;Ll&#313;&#314;&#315;&#316;&#317;&#318;&#319;&#320;&#321;&#322;&#410;&#456;&#564;&#619;&#620;&#621;&#7734;&#7735;&#7736;&#7737;&#7738;&#7739;&#7740;&#7741;Mm&#625;&#7742;&#7743;&#7744;&#7745;&#7746;&#7747;Nn&#209;&#241;&#323;&#324;&#325;&#326;&#327;&#328;&#413;&#626;&#414;&#544;&#459;&#504;&#505;&#565;&#627;&#7748;&#7749;&#7750;&#7751;&#7752;&#7753;&#7754;&#7755;Oo&#210;&#242;&#211;&#243;&#212;&#244;&#213;&#245;&#214;&#246;&#216;&#248;&#332;&#333;&#334;&#335;&#336;&#337;&#415;&#416;&#417;&#465;&#466;&#490;&#491;&#492;&#493;&#510;&#511;&#524;&#525;&#526;&#527;&#554;&#555;&#556;&#557;&#558;&#559;&#560;&#561;&#7756;&#7757;&#7758;&#7759;&#7760;&#7761;&#7762;&#7763;&#7884;&#7885;&#7886;&#7887;&#7888;&#7889;&#7890;&#7891;&#7892;&#7893;&#7894;&#7895;&#7896;&#7897;&#7898;&#7899;&#7900;&#7901;&#7902;&#7903;&#7904;&#7905;&#7906;&#7907;Pp&#420;&#421;&#7764;&#7765;&#7766;&#7767;Qq&#672;Rr&#340;&#341;&#342;&#343;&#344;&#345;&#528;&#529;&#530;&#531;&#636;&#637;&#638;&#7768;&#7769;&#7770;&#7771;&#7772;&#7773;&#7774;&#7775;Ss&#346;&#347;&#348;&#349;&#350;&#351;&#352;&#353;&#536;&#537;&#642;&#7776;&#7777;&#7778;&#7779;&#7780;&#7781;&#7782;&#7783;&#7784;&#7785;Tt&#354;&#355;&#356;&#357;&#358;&#359;&#427;&#428;&#429;&#430;&#648;&#538;&#539;&#566;&#7786;&#7787;&#7788;&#7789;&#7790;&#7791;&#7792;&#7793;&#7831;Uu&#217;&#249;&#218;&#250;&#219;&#251;&#220;&#252;&#360;&#361;&#362;&#363;&#364;&#365;&#366;&#367;&#368;&#369;&#370;&#371;&#431;&#432;&#467;&#468;&#469;&#470;&#471;&#472;&#473;&#474;&#475;&#476;&#532;&#533;&#534;&#535;&#7794;&#7795;&#7796;&#7797;&#7798;&#7799;&#7800;&#7801;&#7802;&#7803;&#7908;&#7909;&#7910;&#7911;&#7912;&#7913;&#7914;&#7915;&#7916;&#7917;&#7918;&#7919;&#7920;&#7921;Vv&#434;&#651;&#7804;&#7805;&#7806;&#7807;Ww&#372;&#373;&#7808;&#7809;&#7810;&#7811;&#7812;&#7813;&#7814;&#7815;&#7816;&#7817;&#7832;Xx&#7818;&#7819;&#7820;&#7821;Yy&#221;&#253;&#255;&#376;&#374;&#375;&#435;&#436;&#562;&#563;&#7822;&#7823;&#7833;&#7922;&#7923;&#7924;&#7925;&#7926;&#7927;&#7928;&#7929;Zz&#377;&#378;&#379;&#380;&#381;&#382;&#437;&#438;&#548;&#549;&#656;&#657;&#7824;&#7825;&#7826;&#7827;&#7828;&#7829;&#7829;'">
        <!ENTITY uppercase "'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBCCCCCCCCCCCCCCCCCDDDDDDDDDDDDDDDDDDDDDDDDEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFFFFFFGGGGGGGGGGGGGGGGGGGGHHHHHHHHHHHHHHHHHHHHIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIJJJJJJKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLLLLLLLLMMMMMMMMMNNNNNNNNNNNNNNNNNNNNNNNNNNNOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOPPPPPPPPQQQRRRRRRRRRRRRRRRRRRRRRRRSSSSSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTTTTTTTUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUVVVVVVVVWWWWWWWWWWWWWWWXXXXXXYYYYYYYYYYYYYYYYYYYYYYYZZZZZZZZZZZZZZZZZZZZZ'">

        <!-- The following three entities are used for sorting and grouping
             indexterms, not in presentation of them. -->
        <!-- If @sortas is present, sort and group by combination of
             @sortas, space and entry to keep it separate from other entries
             with the same @sortas.
             If @sortas is not present, the first item is empty, and the added space
             is removed when normalize-space is applied, leaving just the entry -->

        <!ENTITY primary   'normalize-space(concat(primary/@sortas, " ", primary))'>
        <!ENTITY secondary 'normalize-space(concat(secondary/@sortas, " ", secondary))'>
        <!ENTITY tertiary  'normalize-space(concat(tertiary/@sortas, " ", tertiary))'>

        <!ENTITY section   '(ancestor-or-self::set|ancestor-or-self::book|ancestor-or-self::part|ancestor-or-self::reference|ancestor-or-self::partintro|ancestor-or-self::chapter|ancestor-or-self::appendix|ancestor-or-self::preface|ancestor-or-self::article|ancestor-or-self::section|ancestor-or-self::sect1|ancestor-or-self::sect2|ancestor-or-self::sect3|ancestor-or-self::sect4|ancestor-or-self::sect5|ancestor-or-self::refentry|ancestor-or-self::refsect1|ancestor-or-self::refsect2|ancestor-or-self::refsect3|ancestor-or-self::simplesect|ancestor-or-self::bibliography|ancestor-or-self::glossary|ancestor-or-self::index|ancestor-or-self::webpage|ancestor-or-self::topic)[last()]'>

<!ENTITY section.id 'generate-id(&section;)'>
        <!ENTITY sep '" "'>

        <!ENTITY scope 'count(ancestor::node()|$scope) = count(ancestor::node()) and ($role = @role or $type = @type or (string-length($role) = 0 and string-length($type) = 0))'>

        <!ENTITY setup-language-variable '
        <xsl:variable name="language" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
        <xsl:call-template name="l10n.language"/>
        </xsl:variable>

        <xsl:variable name="lowercase" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
        <xsl:call-template name="gentext">
        <xsl:with-param name="key">normalize.sort.input</xsl:with-param>
        </xsl:call-template>
        </xsl:variable>

        <xsl:variable name="uppercase" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
        <xsl:call-template name="gentext">
        <xsl:with-param name="key">normalize.sort.output</xsl:with-param>
        </xsl:call-template>
        </xsl:variable>
        '>

        <!-- Entity used in html/inline.xsl and fo/inline.xsl -->
        <!ENTITY comment.block.parents "parent::answer|parent::appendix|parent::article|parent::bibliodiv|
        parent::bibliography|parent::blockquote|parent::caution|parent::chapter|
        parent::glossary|parent::glossdiv|parent::important|parent::index|
        parent::indexdiv|parent::listitem|parent::note|parent::orderedlist|
        parent::partintro|parent::preface|parent::procedure|parent::qandadiv|
        parent::qandaset|parent::question|parent::refentry|parent::refnamediv|
        parent::refsect1|parent::refsect2|parent::refsect3|parent::refsection|
        parent::refsynopsisdiv|parent::sect1|parent::sect2|parent::sect3|parent::sect4|
        parent::sect5|parent::section|parent::setindex|parent::sidebar|
        parent::simplesect|parent::taskprerequisites|parent::taskrelated|
        parent::tasksummary|parent::warning|parent::topic">

]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink='http://www.w3.org/1999/xlink'
                xmlns:suwl="http://nwalsh.com/xslt/ext/com.nwalsh.saxon.UnwrapLinks"
                exclude-result-prefixes="xlink suwl"
                version='1.0'>

<!-- ********************************************************************
     $Id: inline.xsl 9663 2012-11-06 19:09:16Z bobstayton $
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://docbook.sf.net/release/xsl/current/ for
     copyright and other information.

     ******************************************************************** -->

<xsl:key name="glossentries" match="glossentry" use="normalize-space(glossterm)"/>
<xsl:key name="glossentries" match="glossentry" use="normalize-space(glossterm/@baseform)"/>

<xsl:template name="simple.xlink">
  <xsl:param name="node" select="."/>
  <xsl:param name="content">
    <xsl:apply-templates/>
  </xsl:param>
  <xsl:param name="linkend" select="$node/@linkend"/>
  <xsl:param name="xhref" select="$node/@xlink:href"/>

  <!-- Support for @xlink:show -->
  <xsl:variable name="target.show">
    <xsl:choose>
      <xsl:when test="$node/@xlink:show = 'new'">_blank</xsl:when>
      <xsl:when test="$node/@xlink:show = 'replace'">_top</xsl:when>
      <xsl:otherwise></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="link">
    <xsl:choose>
      <xsl:when test="$xhref and 
                      (not($node/@xlink:type) or 
                           $node/@xlink:type='simple')">

        <!-- Is it a local idref or a uri? -->
        <xsl:variable name="is.idref">
          <xsl:choose>
            <!-- if the href starts with # and does not contain an "(" -->
            <!-- or if the href starts with #xpointer(id(, it's just an ID -->
            <xsl:when test="starts-with($xhref,'#')
                            and (not(contains($xhref,'&#40;'))
                            or starts-with($xhref,
                                       '#xpointer&#40;id&#40;'))">1</xsl:when>
            <xsl:otherwise>0</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <!-- Is it an olink ? -->
        <xsl:variable name="is.olink">
          <xsl:choose>
            <!-- If xlink:role="http://docbook.org/xlink/role/olink" -->
            <!-- and if the href contains # -->
            <xsl:when test="contains($xhref,'#') and
                 @xlink:role = $xolink.role">1</xsl:when>
            <xsl:otherwise>0</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <xsl:choose>
          <xsl:when test="$is.olink = 1">
            <xsl:call-template name="olink">
              <xsl:with-param name="content" select="$content"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$is.idref = 1">

            <xsl:variable name="idref">
              <xsl:call-template name="xpointer.idref">
                <xsl:with-param name="xpointer" select="$xhref"/>
              </xsl:call-template>
            </xsl:variable>

            <xsl:variable name="targets" select="key('id',$idref)"/>
            <xsl:variable name="target" select="$targets[1]"/>

            <xsl:call-template name="check.id.unique">
              <xsl:with-param name="linkend" select="$idref"/>
            </xsl:call-template>

            <xsl:choose>
              <xsl:when test="count($target) = 0">
                <xsl:message>
                  <xsl:text>XLink to nonexistent id: </xsl:text>
                  <xsl:value-of select="$idref"/>
                </xsl:message>
                <xsl:copy-of select="$content"/>
              </xsl:when>

              <xsl:otherwise>
                <a>
                  <xsl:apply-templates select="." mode="common.html.attributes"/>
                  <xsl:call-template name="id.attribute"/>

                  <xsl:attribute name="href">
                    <xsl:call-template name="href.target">
                      <xsl:with-param name="object" select="$target"/>
                    </xsl:call-template>
                  </xsl:attribute>

                  <xsl:choose>
                    <xsl:when test="$node/@xlink:title">
                      <xsl:attribute name="title">
                        <xsl:value-of select="$node/@xlink:title"/>
                      </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:apply-templates select="$target"
                                           mode="html.title.attribute"/>
                    </xsl:otherwise>
                  </xsl:choose>

                  <xsl:if test="$target.show !=''">
                    <xsl:attribute name="target">
                      <xsl:value-of select="$target.show"/>
                    </xsl:attribute>
                  </xsl:if>

                  <xsl:copy-of select="$content"/>

                </a>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>

          <!-- otherwise it's a URI -->
          <xsl:otherwise>
            <a>
              <xsl:apply-templates select="." mode="common.html.attributes"/>
              <xsl:call-template name="id.attribute"/>
              <xsl:attribute name="href">
                <xsl:value-of select="$xhref"/>
              </xsl:attribute>
              <xsl:if test="$node/@xlink:title">
                <xsl:attribute name="title">
                  <xsl:value-of select="$node/@xlink:title"/>
                </xsl:attribute>
              </xsl:if>

              <!-- For URIs, use @xlink:show if defined, otherwise use ulink.target -->
              <xsl:choose>
                <xsl:when test="$target.show !=''">
                  <xsl:attribute name="target">
                    <xsl:value-of select="$target.show"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:when test="$ulink.target !=''">
                  <xsl:attribute name="target">
                    <xsl:value-of select="$ulink.target"/>
                  </xsl:attribute>
                </xsl:when>
              </xsl:choose>
              
              <xsl:copy-of select="$content"/>
            </a>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <xsl:when test="$linkend">
        <xsl:variable name="targets" select="key('id',$linkend)"/>
        <xsl:variable name="target" select="$targets[1]"/>

        <xsl:call-template name="check.id.unique">
          <xsl:with-param name="linkend" select="$linkend"/>
        </xsl:call-template>

        <a>
          <xsl:apply-templates select="." mode="common.html.attributes"/>
          <xsl:call-template name="id.attribute"/>
          <xsl:attribute name="href">
            <xsl:call-template name="href.target">
              <xsl:with-param name="object" select="$target"/>
            </xsl:call-template>
          </xsl:attribute>

          <xsl:apply-templates select="$target" mode="html.title.attribute"/>

          <xsl:copy-of select="$content"/>
          
        </a>
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy-of select="$content"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="function-available('suwl:unwrapLinks')">
      <xsl:copy-of select="suwl:unwrapLinks($link)"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:copy-of select="$link"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="inline.charseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>
  <!-- * if you want output from the inline.charseq template wrapped in -->
  <!-- * something other than a Span, call the template with some value -->
  <!-- * for the 'wrapper-name' param -->
  <xsl:param name="wrapper-name">span</xsl:param>
  <xsl:element name="{$wrapper-name}">
    <xsl:attribute name="class">
      <xsl:value-of select="local-name(.)"/>
    </xsl:attribute>
    <xsl:call-template name="id.attribute"/>
    <xsl:call-template name="dir"/>
    <xsl:call-template name="generate.html.title"/>
    <xsl:copy-of select="$content"/>
    <xsl:call-template name="apply-annotations"/>
  </xsl:element>
</xsl:template>

<xsl:template name="inline.monoseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>
  <code>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
    <xsl:call-template name="apply-annotations"/>
  </code>
</xsl:template>

<xsl:template name="inline.boldseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>

  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>

    <!-- don't put <strong> inside figure, example, or table titles -->
    <xsl:choose>
      <xsl:when test="local-name(..) = 'title'
                      and (local-name(../..) = 'figure'
                      or local-name(../..) = 'example'
                      or local-name(../..) = 'table')">
        <xsl:copy-of select="$content"/>
      </xsl:when>
      <xsl:otherwise>
        <strong>
          <xsl:copy-of select="$content"/>
        </strong>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:call-template name="apply-annotations"/>
  </span>
</xsl:template>

<xsl:template name="inline.italicseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>
  <em>
    <xsl:call-template name="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
    <xsl:call-template name="apply-annotations"/>
  </em>
</xsl:template>

<xsl:template name="inline.boldmonoseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>
  <!-- don't put <strong> inside figure, example, or table titles -->
  <!-- or other titles that may already be represented with <strong>'s. -->
  <xsl:choose>
    <xsl:when test="local-name(..) = 'title'
                    and (local-name(../..) = 'figure'
                         or local-name(../..) = 'example'
                         or local-name(../..) = 'table'
                         or local-name(../..) = 'formalpara')">
      <code>
        <xsl:call-template name="common.html.attributes"/>
        <xsl:call-template name="id.attribute"/>
        <xsl:copy-of select="$content"/>
        <xsl:call-template name="apply-annotations"/>
      </code>
    </xsl:when>
    <xsl:otherwise>
      <strong>
        <xsl:call-template name="common.html.attributes"/>
        <xsl:call-template name="id.attribute"/>
        <code>
          <xsl:call-template name="generate.html.title"/>
          <xsl:call-template name="dir"/>
          <xsl:copy-of select="$content"/>
        </code>
        <xsl:call-template name="apply-annotations"/>
      </strong>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="inline.italicmonoseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>
  <em>
    <xsl:call-template name="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <code>
      <xsl:call-template name="generate.html.title"/>
      <xsl:call-template name="dir"/>
      <xsl:copy-of select="$content"/>
      <xsl:call-template name="apply-annotations"/>
    </code>
  </em>
</xsl:template>

<xsl:template name="inline.superscriptseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>
  <sup>
    <xsl:call-template name="generate.html.title"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:call-template name="dir"/>
    <xsl:copy-of select="$content"/>
    <xsl:call-template name="apply-annotations"/>
  </sup>
</xsl:template>

<xsl:template name="inline.subscriptseq">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:param>
  <sub>
    <xsl:call-template name="generate.html.title"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:call-template name="dir"/>
    <xsl:copy-of select="$content"/>
    <xsl:call-template name="apply-annotations"/>
  </sub>
</xsl:template>

<!-- ==================================================================== -->
<!-- some special cases -->

<xsl:template match="author">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:call-template name="person.name"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:call-template name="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<xsl:template match="editor">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:call-template name="person.name"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:call-template name="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<xsl:template match="othercredit">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:call-template name="person.name"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:call-template name="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<xsl:template match="authorinitials">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="accel">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="action">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="application">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="classname">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="exceptionname">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="interfacename">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="methodname">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="command">
  <xsl:call-template name="inline.boldseq"/>
</xsl:template>

<xsl:template match="computeroutput">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="constant">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="database">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="date">
  <!-- should this support locale-specific formatting? how? -->
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="errorcode">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="errorname">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="errortype">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="errortext">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="envar">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="filename">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="function">
  <xsl:choose>
    <xsl:when test="$function.parens != '0'
                    and (parameter or function or replaceable)">
      <xsl:variable name="nodes" select="text()|*"/>
      <xsl:call-template name="inline.monoseq">
        <xsl:with-param name="content">
          <xsl:call-template name="simple.xlink">
            <xsl:with-param name="content">
              <xsl:apply-templates select="$nodes[1]"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:with-param>
      </xsl:call-template>
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="$nodes[position()>1]"/>
      <xsl:text>)</xsl:text>
    </xsl:when>
    <xsl:otherwise>
     <xsl:call-template name="inline.monoseq"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="function/parameter" priority="2">
  <xsl:call-template name="inline.italicmonoseq"/>
  <xsl:if test="$function.parens != 0 and following-sibling::*">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="function/replaceable" priority="2">
  <xsl:call-template name="inline.italicmonoseq"/>
  <xsl:if test="$function.parens != 0 and following-sibling::*">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="guibutton">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="guiicon">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="guilabel">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="guimenu">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="guimenuitem">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="guisubmenu">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="hardware">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="interface">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="interfacedefinition">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="keycap">
  <xsl:choose>
    <xsl:when test="@function and normalize-space(.) = ''">
      <xsl:call-template name="inline.boldseq">
        <xsl:with-param name="content">
          <xsl:call-template name="gentext.template">
            <xsl:with-param name="context" select="'keycap'"/>
            <xsl:with-param name="name" select="@function"/>
          </xsl:call-template>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:call-template name="inline.boldseq"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="keycode">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="keysym">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="literal">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="code">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="medialabel">
  <xsl:call-template name="inline.italicseq"/>
</xsl:template>

<xsl:template match="shortcut">
  <xsl:call-template name="inline.boldseq"/>
</xsl:template>

<xsl:template match="mousebutton">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="option">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="package">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="parameter">
  <xsl:call-template name="inline.italicmonoseq"/>
</xsl:template>

<xsl:template match="property">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="prompt">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="replaceable" priority="1">
  <xsl:call-template name="inline.italicmonoseq"/>
</xsl:template>

<xsl:template match="returnvalue">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="structfield">
  <xsl:call-template name="inline.italicmonoseq"/>
</xsl:template>

<xsl:template match="structname">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="symbol">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="systemitem">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="token">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="type">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="userinput">
  <xsl:call-template name="inline.boldmonoseq"/>
</xsl:template>

<xsl:template match="abbrev">
  <xsl:call-template name="inline.charseq">
    <xsl:with-param name="wrapper-name">abbr</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="acronym">
  <xsl:call-template name="inline.charseq">
    <xsl:with-param name="wrapper-name">acronym</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="citerefentry">
  <xsl:choose>
    <xsl:when test="$citerefentry.link != '0'">
      <a>
        <xsl:apply-templates select="." mode="common.html.attributes"/>
        <xsl:attribute name="href">
          <xsl:call-template name="generate.citerefentry.link"/>
        </xsl:attribute>
        <xsl:call-template name="inline.charseq"/>
      </a>
    </xsl:when>
    <xsl:otherwise>
      <xsl:call-template name="inline.charseq"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="generate.citerefentry.link">
  <!-- nop -->
</xsl:template>

<xsl:template name="x.generate.citerefentry.link">
  <xsl:text>http://example.com/cgi-bin/man.cgi?</xsl:text>
  <xsl:value-of select="refentrytitle"/>
  <xsl:text>(</xsl:text>
  <xsl:value-of select="manvolnum"/>
  <xsl:text>)</xsl:text>
</xsl:template>

<xsl:template match="citetitle">
  <xsl:choose>
    <xsl:when test="@pubwork = 'article'">
      <xsl:call-template name="gentext.startquote"/>
      <xsl:call-template name="inline.charseq"/>
      <xsl:call-template name="gentext.endquote"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:call-template name="inline.italicseq"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="emphasis">
  <span>
    <xsl:call-template name="id.attribute"/>
    <xsl:choose>
      <!-- We don't want empty @class values, so do not propagate empty @roles -->
      <xsl:when test="@role  and
                      normalize-space(@role) != '' and
                      $emphasis.propagates.style != 0">
        <xsl:apply-templates select="." mode="common.html.attributes">
          <xsl:with-param name="class" select="@role"/>
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="." mode="common.html.attributes"/>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:call-template name="anchor"/>

    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:choose>
          <xsl:when test="@role = 'bold' or @role='strong'">
            <!-- backwards compatibility: make bold into b elements, but -->
            <!-- don't put bold inside figure, example, or table titles -->
            <xsl:choose>
              <xsl:when test="local-name(..) = 'title'
                              and (local-name(../..) = 'figure'
                              or local-name(../..) = 'example'
                              or local-name(../..) = 'table')">
                <xsl:apply-templates/>
              </xsl:when>
              <xsl:otherwise>
                <strong><xsl:apply-templates/></strong>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:when test="@role and $emphasis.propagates.style != 0">
            <xsl:apply-templates/>
          </xsl:when>
          <xsl:otherwise>
            <em><xsl:apply-templates/></em>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:with-param>
    </xsl:call-template>
  </span>
</xsl:template>

<xsl:template match="foreignphrase">
  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="inline.italicseq"/>
  </span>
</xsl:template>

<xsl:template match="markup">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="phrase">
  <span>
    <xsl:call-template name="id.attribute"/>
    <xsl:call-template name="locale.html.attributes"/>
    <!-- We don't want empty @class values, so do not propagate empty @roles -->
    <xsl:choose>
      <xsl:when test="@role and 
                     normalize-space(@role) != '' and
                     $phrase.propagates.style != 0">
        <xsl:apply-templates select="." mode="class.attribute">
          <xsl:with-param name="class" select="@role"/>
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="." mode="class.attribute"/>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:call-template name="dir"/>
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </span>
</xsl:template>

<xsl:template match="quote">
  <xsl:variable name="depth">
    <xsl:call-template name="dot.count">
      <xsl:with-param name="string">
        <xsl:number level="multiple"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:variable>
  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:choose>
      <xsl:when test="$depth mod 2 = 0">
        <xsl:call-template name="gentext.startquote"/>
        <xsl:call-template name="inline.charseq"/>
        <xsl:call-template name="gentext.endquote"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="gentext.nestedstartquote"/>
        <xsl:call-template name="inline.charseq"/>
        <xsl:call-template name="gentext.nestedendquote"/>
      </xsl:otherwise>
    </xsl:choose>
  </span>
</xsl:template>

<xsl:template match="varname">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<xsl:template match="wordasword">
  <xsl:call-template name="inline.italicseq"/>
</xsl:template>

<xsl:template match="lineannotation">
  <em>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="inline.charseq"/>
  </em>
</xsl:template>

<xsl:template match="superscript">
  <xsl:call-template name="inline.superscriptseq"/>
</xsl:template>

<xsl:template match="subscript">
  <xsl:call-template name="inline.subscriptseq"/>
</xsl:template>

<xsl:template match="trademark">
  <xsl:call-template name="inline.charseq"/>
  <xsl:choose>
    <xsl:when test="@class = 'copyright'
                    or @class = 'registered'">
      <xsl:call-template name="dingbat">
        <xsl:with-param name="dingbat" select="@class"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:when test="@class = 'service'">
      <sup>SM</sup>
    </xsl:when>
    <xsl:otherwise>
      <xsl:call-template name="dingbat">
        <xsl:with-param name="dingbat" select="'trademark'"/>
      </xsl:call-template>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="firstterm">
  <xsl:call-template name="glossterm">
    <xsl:with-param name="firstterm" select="1"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="glossterm" name="glossterm">
  <xsl:param name="firstterm" select="0"/>

  <!-- To avoid extra <a name=""> anchor from inline.italicseq -->
  <xsl:variable name="content">
    <xsl:apply-templates/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="($firstterm.only.link = 0 or $firstterm = 1) and @linkend">
      <xsl:variable name="targets" select="key('id',@linkend)"/>
      <xsl:variable name="target" select="$targets[1]"/>

      <xsl:call-template name="check.id.unique">
        <xsl:with-param name="linkend" select="@linkend"/>
      </xsl:call-template>

      <xsl:choose>
        <xsl:when test="$target">
          <a>
            <xsl:apply-templates select="." mode="common.html.attributes"/>
            <xsl:if test="@id or @xml:id">
              <xsl:attribute name="name">
                <xsl:value-of select="(@id|@xml:id)[1]"/>
              </xsl:attribute>
            </xsl:if>

            <xsl:attribute name="href">
              <xsl:call-template name="href.target">
                <xsl:with-param name="object" select="$target"/>
              </xsl:call-template>
            </xsl:attribute>

            <xsl:call-template name="inline.italicseq">
              <xsl:with-param name="content" select="$content"/>
            </xsl:call-template>
          </a>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="inline.italicseq">
            <xsl:with-param name="content" select="$content"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>

    <xsl:when test="not(@linkend)
                    and ($firstterm.only.link = 0 or $firstterm = 1)
                    and ($glossterm.auto.link != 0)
                    and $glossary.collection != ''">
      <xsl:variable name="term">
        <xsl:choose>
          <xsl:when test="@baseform"><xsl:value-of select="@baseform"/></xsl:when>
          <xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:variable name="cterm"
           select="(document($glossary.collection,.)//glossentry[glossterm=$term])[1]"/>

      <!-- HACK HACK HACK! But it works... -->
      <!-- You'd need to do more work if you wanted to chunk on glossdiv, though -->

      <xsl:variable name="glossary" select="//glossary[@role='auto']"/>

      <xsl:if test="count($glossary) != 1">
        <xsl:message>
          <xsl:text>Warning: glossary.collection specified, but there are </xsl:text>
          <xsl:value-of select="count($glossary)"/>
          <xsl:text> automatic glossaries</xsl:text>
        </xsl:message>
      </xsl:if>

      <xsl:variable name="glosschunk">
        <xsl:call-template name="href.target">
          <xsl:with-param name="object" select="$glossary"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="chunkbase">
        <xsl:choose>
          <xsl:when test="contains($glosschunk, '#')">
            <xsl:value-of select="substring-before($glosschunk, '#')"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$glosschunk"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:choose>
        <xsl:when test="not($cterm)">
          <xsl:message>
            <xsl:text>There's no entry for </xsl:text>
            <xsl:value-of select="$term"/>
            <xsl:text> in </xsl:text>
            <xsl:value-of select="$glossary.collection"/>
          </xsl:message>
          <xsl:call-template name="inline.italicseq"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="id">
            <xsl:call-template name="object.id">
              <xsl:with-param name="object" select="$cterm"/>
            </xsl:call-template>
          </xsl:variable>
          <a href="{$chunkbase}#{$id}">
            <xsl:apply-templates select="." mode="common.html.attributes"/>
            <xsl:call-template name="inline.italicseq">
              <xsl:with-param name="content" select="$content"/>
            </xsl:call-template>
          </a>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>

    <xsl:when test="not(@linkend)
                    and ($firstterm.only.link = 0 or $firstterm = 1)
                    and $glossterm.auto.link != 0">
      <xsl:variable name="term">
        <xsl:choose>
          <xsl:when test="@baseform">
            <xsl:value-of select="normalize-space(@baseform)"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="normalize-space(.)"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="targets"
                    select="key('glossentries', $term)"/>
      <xsl:variable name="target" select="$targets[1]"/>

      <xsl:choose>
        <xsl:when test="count($targets)=0">
          <xsl:message>
            <xsl:text>Error: no glossentry for glossterm: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>.</xsl:text>
          </xsl:message>
          <xsl:call-template name="inline.italicseq"/>
        </xsl:when>
        <xsl:otherwise>
          <a>
            <xsl:apply-templates select="." mode="common.html.attributes"/>
            <xsl:if test="@id or @xml:id">
              <xsl:attribute name="name">
                <xsl:value-of select="(@id|@xml:id)[1]"/>
              </xsl:attribute>
            </xsl:if>

            <xsl:attribute name="href">
              <xsl:call-template name="href.target">
                <xsl:with-param name="object" select="$target"/>
              </xsl:call-template>
            </xsl:attribute>

            <xsl:call-template name="inline.italicseq">
              <xsl:with-param name="content" select="$content"/>
            </xsl:call-template>
          </a>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>

    <xsl:otherwise>
      <xsl:call-template name="inline.italicseq"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="termdef">
  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="gentext.template">
      <xsl:with-param name="context" select="'termdef'"/>
      <xsl:with-param name="name" select="'prefix'"/>
    </xsl:call-template>
    <xsl:apply-templates/>
    <xsl:call-template name="gentext.template">
      <xsl:with-param name="context" select="'termdef'"/>
      <xsl:with-param name="name" select="'suffix'"/>
    </xsl:call-template>
  </span>
</xsl:template>

<xsl:template match="sgmltag|tag">
  <xsl:call-template name="format.sgmltag"/>
</xsl:template>

<xsl:template name="format.sgmltag">
  <xsl:param name="class">
    <xsl:choose>
      <xsl:when test="@class">
        <xsl:value-of select="@class"/>
      </xsl:when>
      <xsl:otherwise>element</xsl:otherwise>
    </xsl:choose>
  </xsl:param>

  <xsl:variable name="content">
    <xsl:choose>
      <xsl:when test="$class='attribute'">
        <xsl:apply-templates/>
      </xsl:when>
      <xsl:when test="$class='attvalue'">
        <xsl:apply-templates/>
      </xsl:when>
      <xsl:when test="$class='element'">
        <xsl:apply-templates/>
      </xsl:when>
      <xsl:when test="$class='endtag'">
        <xsl:text>&lt;/</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>&gt;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='genentity'">
        <xsl:text>&amp;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='numcharref'">
        <xsl:text>&amp;#</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='paramentity'">
        <xsl:text>%</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='pi'">
        <xsl:text>&lt;?</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>&gt;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='xmlpi'">
        <xsl:text>&lt;?</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>?&gt;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='starttag'">
        <xsl:text>&lt;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>&gt;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='emptytag'">
        <xsl:text>&lt;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>/&gt;</xsl:text>
      </xsl:when>
      <xsl:when test="$class='sgmlcomment' or $class='comment'">
        <xsl:text>&lt;!--</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>--&gt;</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <code>
    <xsl:apply-templates select="." mode="common.html.attributes">
      <xsl:with-param name="class" select="concat('sgmltag-', $class)"/>
    </xsl:apply-templates>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content" select="$content"/>
    </xsl:call-template>
  </code>
</xsl:template>

<xsl:template match="email">
  <xsl:call-template name="inline.monoseq">
    <xsl:with-param name="content">
      <xsl:if test="not($email.delimiters.enabled = 0)">
        <xsl:text>&lt;</xsl:text>
      </xsl:if>
      <a>
        <xsl:apply-templates select="." mode="common.html.attributes"/>
        <xsl:call-template name="id.attribute"/>
        <xsl:attribute name="href">
          <xsl:text>mailto:</xsl:text>
          <xsl:value-of select="."/>
        </xsl:attribute>
        <xsl:apply-templates/>
      </a>
      <xsl:if test="not($email.delimiters.enabled = 0)">
        <xsl:text>&gt;</xsl:text>
      </xsl:if>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="keycombo">
  <xsl:variable name="action" select="@action"/>
  <xsl:variable name="joinchar">
    <xsl:choose>
      <xsl:when test="$action='seq'"><xsl:text> </xsl:text></xsl:when>
      <xsl:when test="$action='simul'">+</xsl:when>
      <xsl:when test="$action='press'">-</xsl:when>
      <xsl:when test="$action='click'">-</xsl:when>
      <xsl:when test="$action='double-click'">-</xsl:when>
      <xsl:when test="$action='other'"></xsl:when>
      <xsl:otherwise>+</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:for-each select="*">
    <xsl:if test="position()>1"><xsl:value-of select="$joinchar"/></xsl:if>
    <xsl:apply-templates select="."/>
  </xsl:for-each>
</xsl:template>

<xsl:template match="uri">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="menuchoice">
  <xsl:variable name="shortcut" select="./shortcut"/>
  <xsl:call-template name="process.menuchoice"/>
  <xsl:if test="$shortcut">
    <xsl:text> (</xsl:text>
    <xsl:apply-templates select="$shortcut"/>
    <xsl:text>)</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template name="process.menuchoice">
  <xsl:param name="nodelist" select="guibutton|guiicon|guilabel|guimenu|guimenuitem|guisubmenu|interface"/><!-- not(shortcut) -->
  <xsl:param name="count" select="1"/>

  <xsl:choose>
    <xsl:when test="$count>count($nodelist)"></xsl:when>
    <xsl:when test="$count=1">
      <xsl:apply-templates select="$nodelist[$count=position()]"/>
      <xsl:call-template name="process.menuchoice">
        <xsl:with-param name="nodelist" select="$nodelist"/>
        <xsl:with-param name="count" select="$count+1"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:variable name="node" select="$nodelist[$count=position()]"/>
      <xsl:choose>
        <xsl:when test="local-name($node)='guimenuitem'
                        or local-name($node)='guisubmenu'">
          <xsl:value-of select="$menuchoice.menu.separator"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$menuchoice.separator"/>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates select="$node"/>
      <xsl:call-template name="process.menuchoice">
        <xsl:with-param name="nodelist" select="$nodelist"/>
        <xsl:with-param name="count" select="$count+1"/>
      </xsl:call-template>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="optional">
  <xsl:value-of select="$arg.choice.opt.open.str"/>
  <xsl:call-template name="inline.charseq"/>
  <xsl:value-of select="$arg.choice.opt.close.str"/>
</xsl:template>

<xsl:template match="citation">
  <!-- todo: integrate with bibliography collection -->
  <xsl:variable name="targets" select="(//biblioentry | //bibliomixed)[abbrev = string(current())]"/>
  <xsl:variable name="target" select="$targets[1]"/>

  <xsl:choose>
    <!-- try automatic linking based on match to abbrev -->
    <xsl:when test="$target and not(xref) and not(link)">

      <xsl:text>[</xsl:text>
      <a>
        <xsl:apply-templates select="." mode="common.html.attributes"/>
        <xsl:attribute name="href">
          <xsl:call-template name="href.target">
            <xsl:with-param name="object" select="$target"/>
          </xsl:call-template>
        </xsl:attribute>

        <xsl:choose>
          <xsl:when test="$bibliography.numbered != 0">
            <xsl:call-template name="id.attribute"/>
            <xsl:apply-templates select="$target" mode="citation"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="inline.charseq"/>
          </xsl:otherwise>
        </xsl:choose>

      </a>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:text>[</xsl:text>
      <xsl:call-template name="inline.charseq"/>
      <xsl:text>]</xsl:text>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="citebiblioid">
  <xsl:variable name="targets" select="//*[biblioid = string(current())]"/>
  <xsl:variable name="target" select="$targets[1]"/>

  <xsl:choose>
    <!-- try automatic linking based on match to parent of biblioid -->
    <xsl:when test="$target and not(xref) and not(link)">

      <xsl:text>[</xsl:text>
      <a>
        <xsl:apply-templates select="." mode="common.html.attributes"/>
        <xsl:attribute name="href">
          <xsl:call-template name="href.target">
            <xsl:with-param name="object" select="$target"/>
          </xsl:call-template>
        </xsl:attribute>

        <xsl:call-template name="inline.charseq"/>

      </a>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:text>[</xsl:text>
      <xsl:call-template name="inline.charseq"/>
      <xsl:text>]</xsl:text>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="biblioentry|bibliomixed" mode="citation">
  <xsl:number from="bibliography" count="biblioentry|bibliomixed"
              level="any" format="1"/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="comment[&comment.block.parents;]|remark[&comment.block.parents;]">
  <xsl:if test="$show.comments != 0">
    <p class="remark"><em><xsl:call-template name="inline.charseq"/></em></p>
  </xsl:if>
</xsl:template>

<xsl:template match="comment|remark">
  <xsl:if test="$show.comments != 0">
    <em><xsl:call-template name="inline.charseq"/></em>
  </xsl:if>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="productname">
  <xsl:call-template name="inline.charseq"/>
  <xsl:if test="@class">
    <xsl:call-template name="dingbat">
      <xsl:with-param name="dingbat" select="@class"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

<xsl:template match="productnumber">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="pob|street|city|state|postcode|country|otheraddr">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<xsl:template match="phone|fax">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<!-- in Addresses, for example -->
<xsl:template match="honorific|firstname|surname|lineage|othername">
  <xsl:call-template name="inline.charseq"/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="person">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates select="personname"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<xsl:template match="personname">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:call-template name="person.name"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="org">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<xsl:template match="orgname">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<xsl:template match="orgdiv">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:apply-templates/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<xsl:template match="affiliation">
  <xsl:param name="content">
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="simple.xlink">
      <xsl:with-param name="content">
        <xsl:call-template name="person.name"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:call-template name="apply-annotations"/>
  </xsl:param>

  <span>
    <xsl:apply-templates select="." mode="common.html.attributes"/>
    <xsl:call-template name="id.attribute"/>
    <xsl:copy-of select="$content"/>
  </span>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="beginpage">
  <!-- does nothing; this *is not* markup to force a page break. -->
</xsl:template>

</xsl:stylesheet>

