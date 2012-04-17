# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{jdbc-mysql}
  s.version = "5.1.13"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = [%q{Nick Sieger, Ola Bini and JRuby contributors}]
  s.date = %q{2010-12-09}
  s.description = %q{Install this gem and require 'mysql' within JRuby to load the driver.}
  s.email = %q{nick@nicksieger.com, ola.bini@gmail.com}
  s.extra_rdoc_files = [%q{Manifest.txt}, %q{README.txt}, %q{LICENSE.txt}]
  s.files = [%q{Manifest.txt}, %q{README.txt}, %q{LICENSE.txt}]
  s.homepage = %q{http://jruby-extras.rubyforge.org/ActiveRecord-JDBC}
  s.rdoc_options = [%q{--main}, %q{README.txt}]
  s.require_paths = [%q{lib}]
  s.rubyforge_project = %q{jruby-extras}
  s.rubygems_version = %q{1.8.9}
  s.summary = %q{MySQL JDBC driver for Java and MySQL/ActiveRecord-JDBC.}

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_development_dependency(%q<rubyforge>, [">= 2.0.4"])
    else
      s.add_dependency(%q<rubyforge>, [">= 2.0.4"])
    end
  else
    s.add_dependency(%q<rubyforge>, [">= 2.0.4"])
  end
end
