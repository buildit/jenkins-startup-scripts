setUpGit() {
  git config --local user.name "$GIT_USERNAME"
  git config --local user.email "$GIT_EMAIL"
}

build() {
  gradle build --info
}

deploy() {
  gradle bintrayUpload -PbintrayUser=buildit -PbintrayKey=$BINTRAY_TOKEN -PdryRun=false
}

bump() {
    setUpGit
    git checkout master
    setProperty version $(incrementVersion -m $(getProperty version gradle.properties)) gradle.properties
    git add gradle.properties
    git commit -m "$CD_COMMIT_MESSAGE"
    git push https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$TRAVIS_REPO_SLUG master
}

docs() {
    git checkout master
    gradle -b docs.gradle build
    if git status --porcelain | grep .; then git commit -am "$CD_COMMIT_MESSAGE"; else echo "Documentation unchanged"; fi
    git push https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$TRAVIS_REPO_SLUG master
}

tag() {
  setUpGit
  git tag "$TAG" -m "$LAST_COMMIT_MESSAGE"
  git push https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$TRAVIS_REPO_SLUG $TAG
}

setProperty() {
  awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $3 > $3.tmp
  mv $3.tmp $3
}

getProperty() {
  PROP_KEY=$1
  PROPERTY_FILE=$2
  PROP_VALUE=`cat $PROPERTY_FILE | grep "$PROP_KEY" | cut -d'=' -f2`
  echo $PROP_VALUE
}

incrementVersion() {
  # Parse command line options.

  while getopts ":Mmp" Option
  do
    case $Option in
      M ) major=true;;
      m ) minor=true;;
      p ) patch=true;;
    esac
  done

  shift $(($OPTIND - 1))

  version=$1

  # Build array from version string.

  a=( ${version//./ } )

  # If version string is missing or has the wrong number of members, show usage message.

  if [ ${#a[@]} -ne 3 ]
  then
    echo "usage: $(basename $0) [-Mmp] major.minor.patch"
    exit 1
  fi

  # Increment version numbers as requested.

  if [ ! -z $major ]
  then
    ((a[0]++))
    a[1]=0
    a[2]=0
  fi

  if [ ! -z $minor ]
  then
    ((a[1]++))
    a[2]=0
  fi

  if [ ! -z $patch ]
  then
    ((a[2]++))
  fi

  echo "${a[0]}.${a[1]}.${a[2]}"
}


"$@"
