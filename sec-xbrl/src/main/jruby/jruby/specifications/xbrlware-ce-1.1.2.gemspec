# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{xbrlware-ce}
  s.version = "1.1.2"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = [%q{bitstat technologies}]
  s.date = %q{2010-08-19}
  s.description = %q{A fast, lightweight framework to parse, extract information from XBRL Instance, Taxonomy and Linkbase documents.
xbrlware understands structure and relationship among elements of XBRL documents and
defines a set of APIs for accessing financial & business facts, meta & other related information
defined in XBRL documents.}
  s.email = %q{xbrlware@bitstat.com}
  s.extra_rdoc_files = [%q{Readme.txt}]
  s.files = [%q{Readme.txt}]
  s.homepage = %q{http://www.bitstat.com/xbrlware/}
  s.rdoc_options = [%q{--main}, %q{Readme.txt}]
  s.require_paths = [%q{lib}]
  s.requirements = [%q{xml-simple v1.0.12}]
  s.rubyforge_project = %q{xbrlware}
  s.rubygems_version = %q{1.8.9}
  s.summary = %q{A fast, lightweight framework for automation & analysis of XBRL.}

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<xml-simple>, ["= 1.0.12"])
    else
      s.add_dependency(%q<xml-simple>, ["= 1.0.12"])
    end
  else
    s.add_dependency(%q<xml-simple>, ["= 1.0.12"])
  end
end
